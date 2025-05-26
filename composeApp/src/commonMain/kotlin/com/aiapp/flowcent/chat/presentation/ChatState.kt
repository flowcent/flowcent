package com.aiapp.flowcent.chat.presentation

import com.aiapp.flowcent.data.common.ChatMessage
import com.aiapp.flowcent.data.common.ExpenseItem

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isShimmerLoading: Boolean = false,
    val isCircularLoading: Boolean = false,
    val expenseItems: List<ExpenseItem> = emptyList(),
    val answer: String = "",
    val userText: String = "",
    val originalVoiceText: String = "",
    val translatedVoiceText: String = "",
    val isListening: Boolean = false
)

