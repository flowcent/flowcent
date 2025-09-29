package com.aiapp.flowcent.accounts.presentation.util

fun calculateRemainingPercentage(totalMonthlyExpense: Double, initialBalance: Double): Float {
    if (initialBalance == 0.0) return 0f
    val remaining = initialBalance - totalMonthlyExpense
    return ((remaining / initialBalance) * 100).toInt().toFloat()
}

fun calculateUsedPercentage(totalMonthlyExpense: Double, initialBalance: Double): Float {
    if (initialBalance == 0.0) return 0f
    val usedPercentage = (totalMonthlyExpense / initialBalance) * 100
    return usedPercentage.toInt().toFloat() // discard decimals, keep as Float
}




