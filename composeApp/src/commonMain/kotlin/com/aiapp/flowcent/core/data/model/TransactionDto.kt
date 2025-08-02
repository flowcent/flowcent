package com.aiapp.flowcent.core.data.model

import com.aiapp.flowcent.core.domain.model.ExpenseItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class TransactionDto(
    @Transient val id: String = "",
    val totalAmount: Double = 0.0,
    val category: String = "",
    val createdAt: String = "",
    val createdBy: String = "",
    val transactionId: String = "",
    val updatedAt: String = "",
    val updatedBy: String = "",
    val uid: String = "",
    val expenses: List<ExpenseItem> = emptyList()
)
