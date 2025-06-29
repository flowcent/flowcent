package com.aiapp.flowcent.core.data.repository

import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.util.Resource

interface ExpenseRepository {
    suspend fun saveExpenseItemsToDb(expenseItems: List<ExpenseItem>): Resource<String>
    suspend fun getAllExpenses(): Resource<List<ExpenseItem>>
}