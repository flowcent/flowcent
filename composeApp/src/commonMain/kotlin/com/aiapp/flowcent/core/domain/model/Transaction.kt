package com.aiapp.flowcent.core.domain.model

data class Transaction(
    val id: String = "",
    val totalAmount: Double = 0.0,
    val totalExpenseAmount: Double = 0.0,
    val totalIncomeAmount: Double = 0.0,
    val category: String = "",
    val createdAt: Long = 0L,
    val createdBy: String = "",
    val transactionId: String = "",
    val updatedAt: Long = 0L,
    val updatedBy: String = "",
    val uid: String = "",
    val expenses: List<ExpenseItem> = emptyList(),
    val creatorUserName: String = ""
)
