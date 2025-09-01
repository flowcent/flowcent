package com.aiapp.flowcent.subscription.presentation

import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.subscription.domain.SubscriptionPlan

data class SubscriptionState(
    val currentPlan: SubscriptionPlan = SubscriptionPlan.STANDARD,
    val totalTransactions: Int = 0,
    val totalAiChats: Int = 0,
    val totalSharedAccounts: Int = 0,
    val uid: String = "",
    val user: User? = null
)
