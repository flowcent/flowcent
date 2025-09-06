package com.aiapp.flowcent.subscription.util

import com.aiapp.flowcent.auth.data.model.ActiveEntitlement
import com.aiapp.flowcent.subscription.data.FeatureLimits
import com.aiapp.flowcent.subscription.data.SubscriptionFeatures
import com.aiapp.flowcent.subscription.domain.SubscriptionPlan
import com.revenuecat.purchases.kmp.models.CustomerInfo

object SubscriptionUtil {
    const val LITE_ENTITLEMENT_ID = "Lite"
    const val PRO_ENTITLEMENT_ID = "Pro"
    const val FREE_ENTITLEMENT_ID = "Free"

    fun getFeaturesByUser(plan: SubscriptionPlan): FeatureLimits? {
        return SubscriptionFeatures.planLimits[plan]
    }

    fun getLevelString(plan: SubscriptionPlan): String {
        return when (plan) {
            SubscriptionPlan.Free -> "Free"
            SubscriptionPlan.Lite -> "Lite"
            SubscriptionPlan.Pro -> "Pro"
        }
    }


    fun getActiveEntitlement(customerInfo: CustomerInfo): ActiveEntitlement {
        val entitlements = customerInfo.entitlements

        return when {
            entitlements[PRO_ENTITLEMENT_ID]?.isActive == true -> {
                ActiveEntitlement(SubscriptionPlan.Pro, entitlements[PRO_ENTITLEMENT_ID])
            }

            entitlements[LITE_ENTITLEMENT_ID]?.isActive == true -> {
                ActiveEntitlement(SubscriptionPlan.Lite, entitlements[LITE_ENTITLEMENT_ID])
            }

            else -> {
                ActiveEntitlement(SubscriptionPlan.Free, null)
            }
        }
    }
}