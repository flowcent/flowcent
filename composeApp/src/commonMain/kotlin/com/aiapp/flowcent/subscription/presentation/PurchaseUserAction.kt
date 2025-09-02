package com.aiapp.flowcent.subscription.presentation

import com.aiapp.flowcent.subscription.domain.SubscriptionPlan

sealed interface PurchaseUserAction {
    data object CheckCurrentPlan : PurchaseUserAction
    data class UpdateCurrentPlan(val uid: String, val currentPlan: SubscriptionPlan) :
        PurchaseUserAction

    data class RegisterPurchaseUserId(val uid: String, val flowCentUserId: String) :
        PurchaseUserAction

    data class FetchMonthlyTransactionCount(val uid: String) : PurchaseUserAction
    data class FetchMonthlyAIChatsCount(val uid: String) : PurchaseUserAction
    data class FetchSharedAccountsCount(val uid: String) : PurchaseUserAction
}