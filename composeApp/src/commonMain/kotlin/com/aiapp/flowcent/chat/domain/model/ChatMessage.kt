package com.aiapp.flowcent.chat.domain.model

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val expenseItems: List<ExpenseItem>
)
