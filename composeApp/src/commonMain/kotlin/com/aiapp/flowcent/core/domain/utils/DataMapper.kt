package com.aiapp.flowcent.core.domain.utils

import com.aiapp.flowcent.core.data.model.ExpenseItemDto
import com.aiapp.flowcent.core.data.model.TransactionDto
import com.aiapp.flowcent.core.domain.model.ExpenseItem
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
        expenses = expenses.map { it.toExpenseItem() }
    )
}

fun List<TransactionDto>.toTransactions(): List<Transaction> {
    return this.map { it.toTransaction() }
}

fun ExpenseItemDto.toExpenseItem(): ExpenseItem {
    return ExpenseItem(
        amount = amount,
        category = category,
        title = title,
        type = EnumConstants.TransactionType.valueOf(type.uppercase()),
        involvedParty = involvedParty
    )
}

fun ExpenseItem.toExpenseItemDto(): ExpenseItemDto {
    return ExpenseItemDto(
        amount = amount,
        category = category,
        title = title,
        type = type.name,
        involvedParty = involvedParty
    )
}