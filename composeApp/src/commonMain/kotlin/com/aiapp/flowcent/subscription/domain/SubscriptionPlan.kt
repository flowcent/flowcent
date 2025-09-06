package com.aiapp.flowcent.subscription.domain

sealed class SubscriptionPlan {
    object Pro : SubscriptionPlan()
    object Lite : SubscriptionPlan()
    object Free : SubscriptionPlan()
}