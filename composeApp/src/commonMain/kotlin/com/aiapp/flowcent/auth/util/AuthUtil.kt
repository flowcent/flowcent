package com.aiapp.flowcent.auth.util

import com.aiapp.flowcent.subscription.data.FeatureLimits

 fun calculateRemainingCredits(
    usedTransactions: Int,
    usedAiChats: Int,
    features: FeatureLimits?
): Pair<Int, Int> {
    val remainingTransactions = features?.maxTransactionsPerMonth
        ?.let { it - usedTransactions }
        ?.coerceAtLeast(0) ?: 0

    val remainingAiChats = features?.maxAiChatsPerMonth
        ?.let { it - usedAiChats }
        ?.coerceAtLeast(0) ?: 0

    return remainingTransactions to remainingAiChats
}