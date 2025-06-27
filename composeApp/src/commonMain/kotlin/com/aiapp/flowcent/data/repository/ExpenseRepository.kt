package com.aiapp.flowcent.data.repository

import com.aiapp.flowcent.data.request.ExpenseItem
import com.aiapp.flowcent.util.Resource

interface ExpenseRepository {
    suspend fun saveExpenseItemsToDb(expenseItems: List<ExpenseItem>): Resource<String>
}