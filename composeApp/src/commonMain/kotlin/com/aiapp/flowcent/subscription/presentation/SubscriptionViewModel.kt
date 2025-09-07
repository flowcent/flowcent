package com.aiapp.flowcent.subscription.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.auth.data.model.Subscription
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.domain.utils.Resource
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils
import com.aiapp.flowcent.subscription.domain.SubscriptionPlan
import com.aiapp.flowcent.subscription.util.SubscriptionUtil
import com.aiapp.flowcent.subscription.util.SubscriptionUtil.FREE_ENTITLEMENT_ID
import com.aiapp.flowcent.subscription.util.SubscriptionUtil.LITE_ENTITLEMENT_ID
import com.aiapp.flowcent.subscription.util.SubscriptionUtil.PRO_ENTITLEMENT_ID
import com.aiapp.flowcent.subscription.util.SubscriptionUtil.getActiveEntitlement
import com.aiapp.flowcent.subscription.util.SubscriptionUtil.getLevelString
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.EntitlementInfos
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.PurchasesErrorCode
import com.revenuecat.purchases.kmp.models.PurchasesException
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubscriptionViewModel(
    private val authRepository: AuthRepository, private val prefRepository: PrefRepository
) : ViewModel() {
    private val _subscriptionState = MutableStateFlow(SubscriptionState())
    val subscriptionState: StateFlow<SubscriptionState> = _subscriptionState

    init {
        viewModelScope.launch {
            prefRepository.uid.collect { uidFromDataStore ->
                _subscriptionState.update { currentState ->
                    currentState.copy(uid = uidFromDataStore ?: "")
                }
            }
        }
    }

    fun onAction(action: PurchaseUserAction) {
        when (action) {
            is PurchaseUserAction.UpdateCurrentPlan -> {
                viewModelScope.launch {
                    try {
                        val activeEntitlement = getActiveEntitlement(action.customerInfo)

                        _subscriptionState.update {
                            it.copy(
                                currentSubscriptionPlan = activeEntitlement.plan,
                                currentPlanId = activeEntitlement.entitlement?.productPlanIdentifier
                                    ?: "free"
                            )
                        }

                        if (action.requireUpdate) {
                            updateUserSubscription(
                                action.uid,
                                activeEntitlement.entitlement?.identifier ?: FREE_ENTITLEMENT_ID,
                                activeEntitlement.plan,
                                action.customerInfo
                            )
                        }
                    } catch (e: PurchasesException) {
                        if (e.code == PurchasesErrorCode.NetworkError) {
                            Napier.e("Sohan NetworkError in purchasing ${e.code}")
                        }
                    }
                }
            }
        }
    }

    private suspend fun updateUserSubscription(
        uid: String,
        id: String,
        plan: SubscriptionPlan,
        customerInfo: CustomerInfo,
    ) {
        val entitlement = customerInfo.entitlements[id]

        val subscription = Subscription(
            currentPlan = getLevelString(plan),
            currentPlanId = entitlement?.productPlanIdentifier ?: "free",
            currentEntitlementId = entitlement?.identifier ?: FREE_ENTITLEMENT_ID,
            currentPlanStartDate = entitlement?.latestPurchaseDateMillis
                ?: DateTimeUtils.getCurrentTimeInMilli(),
            currentPlanExpiryDate = entitlement?.expirationDateMillis ?: 0L,
            revenueCatDeviceId = customerInfo.originalAppUserId,
            revenueCatAppUserId = Purchases.sharedInstance.appUserID,
            willRenew = entitlement?.willRenew ?: false,
            storeName = entitlement?.store?.name ?: "",
        )

        val result = authRepository.updateUserSubscription(
            uid,
            subscription
        )
        when (result) {
            is Resource.Success -> {
                Napier.e("Sohan User subscription updated successfully")
            }

            is Resource.Error -> {
                Napier.e("Sohan Error in updating user subscription: ${result.message}")
            }

            Resource.Loading -> {}
        }
    }
}