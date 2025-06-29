package com.aiapp.flowcent.chat.domain.model

import com.aiapp.flowcent.core.domain.model.ExpenseItem

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val expenseItems: List<ExpenseItem>
)
