package com.aiapp.flowcent.subscription.util

import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.ktx.awaitCustomerInfo
import io.github.aakira.napier.Napier

object SubscriptionUtil {
    val ENTITLEMENT_IDENTIFIER = "Lite"

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

}