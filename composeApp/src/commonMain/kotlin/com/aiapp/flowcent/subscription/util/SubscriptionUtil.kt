package com.aiapp.flowcent.subscription.util

import com.aiapp.flowcent.subscription.data.FeatureLimits
import com.aiapp.flowcent.subscription.data.SubscriptionFeatures
import com.aiapp.flowcent.subscription.domain.SubscriptionPlan
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.ktx.awaitCustomerInfo
import io.github.aakira.napier.Napier

object SubscriptionUtil {
    const val LITE_ENTITLEMENT_IDENTIFIER = "Lite"
    const val PRO_ENTITLEMENT_IDENTIFIER = "Pro"

    const val LITE_PREMIUM_MONTHLY_SUBSCRIPTION_IDENTIFIER = "lite_premium_subscription:lite-premium-base"
    const val LITE_PREMIUM_YEARLY_SUBSCRIPTION_IDENTIFIER = "lite_premium_yearly:lite-premium-yearly-base"
    const val PRO_PREMIUM_MONTHLY_SUBSCRIPTION_IDENTIFIER = "pro_premium_subscription:pro-premium-base"
    const val PRO_PREMIUM_YEARLY_SUBSCRIPTION_IDENTIFIER = "pro_premium_yearly:pro-premium-yearly-base"

    suspend fun isUserEntitled(entitlementId: String): Boolean {
        return try {
            val customerInfo = Purchases.sharedInstance.awaitCustomerInfo()
            Napier.e("Sohan appUserID ${Purchases.sharedInstance.appUserID}")
            Napier.e("Sohan customerInfo $customerInfo")
            customerInfo.entitlements[entitlementId]?.isActive == true
        } catch (e: Exception) {
            // log error if needed
            false
        }
    }

    private fun getFeaturesByUser(plan: SubscriptionPlan): FeatureLimits? {
        return SubscriptionFeatures.planLimits[plan]
    }

    fun canAddTransaction(subscriptionPlan: SubscriptionPlan, currentTransaction: Int): Boolean {
        val limits = getFeaturesByUser(subscriptionPlan)
        return limits?.maxTransactionsPerMonth?.let { currentTransaction < it } ?: true
    }

}