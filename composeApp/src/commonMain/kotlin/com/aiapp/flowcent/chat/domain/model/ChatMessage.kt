package com.aiapp.flowcent.chat.domain.model

import com.aiapp.flowcent.data.request.ExpenseItem

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val expenseItems: List<ExpenseItem>
)
