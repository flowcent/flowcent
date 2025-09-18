package com.aiapp.flowcent.userOnboarding

import com.aiapp.flowcent.chat.domain.model.ChatHistory
import com.aiapp.flowcent.core.domain.model.ExpenseItem

data class UserOnboardingState(
    val isLoading: Boolean = true,
    val histories: List<ChatHistory> = emptyList(),
    val isSendingMessage: Boolean = false,
    val userText: String = "",
    val uid: String = "",
    val expenseItems: List<ExpenseItem> = emptyList(),
    val isListening: Boolean = false,
    val showSaveButton: Boolean = false
)
