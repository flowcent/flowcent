package com.aiapp.flowcent.core.domain.utils

import com.aiapp.flowcent.core.data.model.TransactionDto
import com.aiapp.flowcent.core.domain.model.Transaction

fun TransactionDto.toTransaction(): Transaction {
    return Transaction(
        id = id,
        totalAmount = totalAmount,
        category = category,
        createdAt = createdAt,
        createdBy = createdBy,
        transactionId = transactionId,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        uid = uid,
        expenses = expenses
    )
}

fun List<TransactionDto>.toTransactions(): List<Transaction> {
    return this.map { it.toTransaction() }
}