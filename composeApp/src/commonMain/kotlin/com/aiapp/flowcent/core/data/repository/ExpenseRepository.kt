package com.aiapp.flowcent.core.data.repository

import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.util.Resource

interface ExpenseRepository {
    suspend fun saveExpenseItemsToDb(uid: String, expenseItems: List<ExpenseItem>): Resource<String>
    suspend fun getAllExpenses(uid: String): Resource<List<ExpenseItem>>
    suspend fun getDailyExpenses(uid: String, dateString: String): Resource<List<ExpenseItem>>
    suspend fun totalAmount(uid: String): Resource<Int>
    suspend fun totalExpenses(uid: String): Resource<Int>
    suspend fun totalIncome(uid: String): Resource<Int>
}