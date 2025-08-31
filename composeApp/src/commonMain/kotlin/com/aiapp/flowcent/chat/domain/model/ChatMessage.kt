package com.aiapp.flowcent.chat.domain.model

import com.aiapp.flowcent.core.domain.model.ExpenseItem

data class ChatMessage(
    val id: String = "",
    val text: String = "",
    val isUser: Boolean = false,
    val expenseItems: List<ExpenseItem> = emptyList(),
    val isBotMessageLoading: Boolean = true
)
