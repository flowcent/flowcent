package com.aiapp.flowcent.subscription.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.domain.utils.Resource
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

    fun onAction(action: UserAction) {
        when (action) {
            UserAction.CheckCurrentPlan -> {
                Purchases.sharedInstance.logIn(
                    _subscriptionState.value.uid, ::showError
                ) { customerInfo, created ->
                    handleCustomerInfo(customerInfo)
                }
            }

            is UserAction.UpdateCurrentPlan -> {
                Purchases.sharedInstance.delegate = object : PurchasesDelegate {
                    override fun onCustomerInfoUpdated(customerInfo: CustomerInfo) {
                        handleCustomerInfo(customerInfo, true)
                    }

                    override fun onPurchasePromoProduct(
                        product: StoreProduct, startPurchase: (
                            onError: (error: PurchasesError, userCancelled: Boolean) -> Unit,
                            onSuccess: (storeTransaction: StoreTransaction, customerInfo: CustomerInfo) -> Unit
                        ) -> Unit
                    ) {

                    }
                }

            }

            is UserAction.FetchUserUId -> {
                viewModelScope.launch {
                    if (_subscriptionState.value.uid.isNotEmpty()) {
                        fetchUserProfile(_subscriptionState.value.uid)
                    } else {
                        prefRepository.uid.collect { uidFromDataStore ->
                            _subscriptionState.update { currentState ->
                                currentState.copy(uid = uidFromDataStore ?: "")
                            }
                            fetchUserProfile(uid = uidFromDataStore)
                        }
                    }
                }
            }
        }
    }

    private fun handleCustomerInfo(customerInfo: CustomerInfo, requireUpdate: Boolean = false) {
        Napier.e("Sohan → onCustomerInfoUpdated originalAppUserId: ${customerInfo.originalAppUserId}")

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

        Napier.e("Sohan Subscription currentPlan: $currentPlan")
    }

    private fun showError(purchasesError: PurchasesError) {

    }

    private suspend fun fetchUserProfile(uid: String?) {
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
            }
        }
    }
}