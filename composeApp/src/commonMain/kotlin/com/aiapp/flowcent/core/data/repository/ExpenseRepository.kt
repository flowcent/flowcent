package com.aiapp.flowcent.core.data.repository

import com.aiapp.flowcent.core.data.model.TransactionDto
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.util.Resource

interface ExpenseRepository {
    suspend fun saveExpenseItemsToDb(uid: String, transactionDto: TransactionDto): Resource<String>
    suspend fun getAllExpenses(uid: String): Resource<List<ExpenseItem>>
    suspend fun getDailyExpenses(uid: String, dateString: String): Resource<List<TransactionDto>>
    suspend fun totalAmount(uid: String): Resource<Double>
    suspend fun totalExpenses(uid: String): Resource<Double>
    suspend fun totalIncome(uid: String): Resource<Double>
}