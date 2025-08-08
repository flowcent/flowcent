package com.aiapp.flowcent.chat.presentation

import com.aiapp.flowcent.chat.domain.model.AccountSelectionType
import com.aiapp.flowcent.chat.domain.model.ChatMessage
import com.aiapp.flowcent.core.domain.model.ExpenseItem

data class ChatState(
    val messages: List<ChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val expenseItems: List<ExpenseItem> = emptyList(),
    val answer: String = "",
    val userText: String = "",
    val originalVoiceText: String = "",
    val translatedVoiceText: String = "",
    val isListening: Boolean = false,
    val showSendButton: Boolean = false,
    val isSendingMessage: Boolean = false,
    val uid: String = "",
    val selectionType: AccountSelectionType = AccountSelectionType.PERSONAL,
    val showAccounts: Boolean = false
)

