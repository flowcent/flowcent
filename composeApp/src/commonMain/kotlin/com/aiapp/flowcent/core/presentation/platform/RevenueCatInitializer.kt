package com.aiapp.flowcent.core.presentation.platform

import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesConfiguration
import io.github.aakira.napier.Napier

object RevenueCatInitializer {
    fun initializeRevenueCat() {
        Purchases.logLevel = LogLevel.DEBUG
        Napier.e("Sohan getRevenueCatApiKey() ${getRevenueCatApiKey()}")
        Purchases.configure(
            PurchasesConfiguration(
                apiKey = getRevenueCatApiKey()
            )
        )
    }
}