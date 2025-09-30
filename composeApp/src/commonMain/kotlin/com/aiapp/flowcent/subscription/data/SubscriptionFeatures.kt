package com.aiapp.flowcent.subscription.data

import com.aiapp.flowcent.subscription.domain.SubscriptionPlan


data class FeatureLimits(
    val maxTransactionsPerMonth: Int?,
    val maxAiChatsPerMonth: Int?,
    val maxSharedAccountsPerMonth: Int?,
    val canExportReports: Boolean,
    val aiAssistantEnabled: Boolean
)

object SubscriptionFeatures {
    val planLimits: Map<SubscriptionPlan, FeatureLimits> = mapOf(
        SubscriptionPlan.Free to FeatureLimits(
            maxTransactionsPerMonth = 50,
            maxAiChatsPerMonth = 100,
            maxSharedAccountsPerMonth = 1,
            canExportReports = false,
            aiAssistantEnabled = true
        ),
        SubscriptionPlan.Lite to FeatureLimits(
            maxTransactionsPerMonth = 150,
            maxAiChatsPerMonth = 300,
            maxSharedAccountsPerMonth = 3,
            canExportReports = true,
            aiAssistantEnabled = true
        ),
        SubscriptionPlan.Pro to FeatureLimits(
            maxTransactionsPerMonth = null,  // unlimited
            maxAiChatsPerMonth = null,
            maxSharedAccountsPerMonth = null,
            canExportReports = true,
            aiAssistantEnabled = true
        ),
    )
}
