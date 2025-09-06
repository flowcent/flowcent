package com.aiapp.flowcent.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Subscription(
    val currentPlan: String = "",
    val currentPlanId: String = "",
    val currentEntitlementId: String = "",
    val currentPlanStartDate: Long = 0L,
    val currentPlanExpiryDate: Long = 0L,
    val revenueCatDeviceId: String = "",
    val revenueCatAppUserId: String = "",
    val willRenew: Boolean = false,
    val storeName: String = "",
)
