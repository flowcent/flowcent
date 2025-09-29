package com.aiapp.flowcent.core.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class TransactionDto(
    @Transient val id: String = "",
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
    val expenses: List<ExpenseItemDto> = emptyList(),
    val creatorUserName: String = ""
)
