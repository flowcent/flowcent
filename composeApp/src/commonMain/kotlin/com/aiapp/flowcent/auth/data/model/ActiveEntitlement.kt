package com.aiapp.flowcent.auth.data.model

import com.aiapp.flowcent.subscription.domain.SubscriptionPlan
import com.revenuecat.purchases.kmp.models.EntitlementInfo

data class ActiveEntitlement(
    val plan: SubscriptionPlan,
    val entitlement: EntitlementInfo?
)
