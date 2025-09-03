package com.aiapp.flowcent.subscription.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.domain.utils.Resource
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils
import com.aiapp.flowcent.subscription.domain.SubscriptionPlan
import com.aiapp.flowcent.subscription.util.SubscriptionUtil
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesDelegate
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreProduct
import com.revenuecat.purchases.kmp.models.StoreTransaction
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


    fun onAction(action: PurchaseUserAction) {
        when (action) {
            PurchaseUserAction.CheckCurrentPlan -> {}

            is PurchaseUserAction.UpdateCurrentPlan -> {
                viewModelScope.launch {
                    if (_subscriptionState.value.uid.isNotEmpty()) {
                        fetchUserProfile(_subscriptionState.value.uid, action.customerInfo)
                    } else {
                        prefRepository.uid.collect { uidFromDataStore ->
                            _subscriptionState.update { currentState ->
                                currentState.copy(uid = uidFromDataStore ?: "")
                            }
                            fetchUserProfile(uid = uidFromDataStore, action.customerInfo)
                        }
                    }
                }
            }

            is PurchaseUserAction.RegisterPurchaseUserId -> {

            }

            is PurchaseUserAction.FetchMonthlyAIChatsCount -> TODO()
            is PurchaseUserAction.FetchMonthlyTransactionCount -> TODO()
            is PurchaseUserAction.FetchSharedAccountsCount -> TODO()
        }
    }

    private fun registerAppUserId(uid: String, flowCentUserId: String) {
        Purchases.sharedInstance.logIn(
            flowCentUserId, ::showError
        ) { customerInfo, created ->
            Napier.e("Sohan registerAppUserId created: $created")
            handleCustomerInfo(uid, customerInfo, true);
        }
    }

    private fun handleCustomerInfo(
        uid: String, customerInfo: CustomerInfo, requireUpdate: Boolean = false
    ) {
        Napier.e("Sohan → handleCustomerInfo originalAppUserId ${customerInfo.originalAppUserId}")
        val activeSubs = customerInfo.activeSubscriptions
        Napier.e("Sohan → Active subscriptions: $activeSubs")

        val currentPlan = when {
            activeSubs.contains(SubscriptionUtil.LITE_PREMIUM_MONTHLY_SUBSCRIPTION_IDENTIFIER) || activeSubs.contains(
                SubscriptionUtil.LITE_PREMIUM_YEARLY_SUBSCRIPTION_IDENTIFIER
            ) -> SubscriptionPlan.LITE

            activeSubs.contains(SubscriptionUtil.PRO_PREMIUM_MONTHLY_SUBSCRIPTION_IDENTIFIER) || activeSubs.contains(
                SubscriptionUtil.PRO_PREMIUM_YEARLY_SUBSCRIPTION_IDENTIFIER
            ) -> SubscriptionPlan.PRO

            else -> SubscriptionPlan.STANDARD
        }

        viewModelScope.launch {
            _subscriptionState.update {
                it.copy(
                    currentPlan = currentPlan
                )
            }

            if (requireUpdate) {
                updateUserCurrentPlan(
                    uid,
                    currentPlan.name,
                    "",
                    customerInfo.originalAppUserId,
                    customerInfo.originalAppUserId
                )
            }
        }

        Napier.e("Sohan Subscription currentPlan: $currentPlan")
    }

    private suspend fun updateUserCurrentPlan(
        uid: String,
        currentPlan: String,
        currentPlanId: String,
        revenueCatDeviceId: String,
        revenueCatAppUserId: String
    ) {
        when (authRepository.updateUserSubscription(
            uid = uid,
            currentPlan = currentPlan,
            expiryDate = DateTimeUtils.getCurrentTimeInMilli(),
            currentPlanId = currentPlanId,
            revenueCatDeviceId = revenueCatDeviceId,
            revenueCatAppUserId = revenueCatAppUserId
        )) {
            is Resource.Error -> {
                Napier.e("Sohan plan updated failed")
            }

            Resource.Loading -> {}
            is Resource.Success<*> -> {
                Napier.e("Sohan plan updated successfully")
            }
        }
    }

    private fun showError(purchasesError: PurchasesError) {
        Napier.e("Sohan → showError: $purchasesError")

    }

    private suspend fun fetchUserProfile(uid: String?, customerInfo: CustomerInfo) {
        if (uid.isNullOrEmpty()) {
            Napier.e("Sohan 404 No User Found")
            return
        }
        when (val result = authRepository.fetchUserProfile(uid)) {
            is Resource.Error -> {
                println("Sohan Error in fetching user profile: ${result.message}")
            }

            Resource.Loading -> {}
            is Resource.Success -> {
                _subscriptionState.update {
                    it.copy(
                        user = result.data,
                    )
                }

                result.data?.flowCentUserId?.let { flowCentUserId ->
                    if (flowCentUserId.isNotEmpty() && flowCentUserId != customerInfo.originalAppUserId) {
                        registerAppUserId(uid, result.data.flowCentUserId)
                    }
                } ?: run {
                    handleCustomerInfo(uid, customerInfo, true);
                }
            }
        }
    }
}