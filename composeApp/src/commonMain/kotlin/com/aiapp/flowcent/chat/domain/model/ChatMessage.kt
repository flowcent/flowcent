package com.aiapp.flowcent.chat.domain.model

import com.aiapp.flowcent.core.domain.model.ExpenseItem
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class ChatMessage @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String = Uuid.random().toString(),
    val text: String = "",
    val isUser: Boolean = false,
    val expenseItems: List<ExpenseItem> = emptyList(),
    val isLoading: Boolean = true
)
