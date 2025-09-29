package com.aiapp.flowcent.home.util

import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.domain.utils.EnumConstants
import kotlin.math.abs


fun dailyExpenseBudget(userInitialBalance: Double, userSavingTarget: Double): Double {
    try {
        val totalMonthlyBudget =
            userInitialBalance.toFloat() - userSavingTarget.toFloat()
        return totalMonthlyBudget / 30.0
    } catch (ex: Exception) {
        return 0.0
    }
}

fun calculateDailyTotalSpend(expenseList: List<List<ExpenseItem>>): Double {
    return expenseList
        .flatten()
        .filter { it.type == EnumConstants.TransactionType.EXPENSE }
        .sumOf { it.amount }
}

fun calculateDailyTotalIncome(expenseList: List<List<ExpenseItem>>): Double {
    return expenseList
        .flatten()
        .filter { it.type == EnumConstants.TransactionType.INCOME }
        .sumOf { it.amount }
}

fun calculateDailySavingTarget(savingTarget: Double?): Float {
    return ((savingTarget ?: 3000.0) / 30).toFloat()
}


fun calculateDailyActualSaving(
    dailyTotalSpend: Double,
    userInitialBalance: Double,
    userSavingTarget: Double
): Float {
    val dailyExpenseBudget = dailyExpenseBudget(userInitialBalance, userSavingTarget)

    return if (dailyTotalSpend > dailyExpenseBudget) {
        0f
    } else {
        abs(dailyExpenseBudget - dailyTotalSpend).toFloat()
    }
}
