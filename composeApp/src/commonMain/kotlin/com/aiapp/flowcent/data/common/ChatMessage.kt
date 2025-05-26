package com.aiapp.flowcent.data.common

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val expenseItems: List<ExpenseItem>
)
