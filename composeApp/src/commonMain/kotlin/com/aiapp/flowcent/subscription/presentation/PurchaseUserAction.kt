package com.aiapp.flowcent.subscription.presentation

import com.revenuecat.purchases.kmp.models.CustomerInfo

sealed interface PurchaseUserAction {
    data object CheckCurrentPlan : PurchaseUserAction
    data class UpdateCurrentPlan(val customerInfo: CustomerInfo) :
        PurchaseUserAction

    data class RegisterPurchaseUserId(val uid: String, val flowCentUserId: String) :
        PurchaseUserAction

    data class FetchMonthlyTransactionCount(val uid: String) : PurchaseUserAction
    data class FetchMonthlyAIChatsCount(val uid: String) : PurchaseUserAction
    data class FetchSharedAccountsCount(val uid: String) : PurchaseUserAction
}