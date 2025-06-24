package com.aiapp.flowcent.chat.presentation

import com.aiapp.flowcent.chat.domain.model.ChatMessage
import com.aiapp.flowcent.chat.domain.model.ExpenseItem

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
    val isListening: Boolean = false,
    val showSendButton: Boolean = false
)

