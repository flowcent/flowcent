package com.aiapp.flowcent.subscription.presentation

import com.revenuecat.purchases.kmp.models.CustomerInfo

sealed interface PurchaseUserAction {
    data class UpdateCurrentPlan(
        val uid: String,
        val customerInfo: CustomerInfo,
        val requireUpdate: Boolean
    ) :
        PurchaseUserAction
}