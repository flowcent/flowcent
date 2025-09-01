package com.aiapp.flowcent.subscription.presentation

import com.aiapp.flowcent.subscription.domain.SubscriptionPlan

sealed interface UserAction {
    data object CheckCurrentPlan : UserAction
    data class UpdateCurrentPlan(val currentPlan: SubscriptionPlan) : UserAction
    data object FetchUserUId : UserAction
}