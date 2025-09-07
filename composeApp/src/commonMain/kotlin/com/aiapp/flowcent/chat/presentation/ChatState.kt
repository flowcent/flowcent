package com.aiapp.flowcent.chat.presentation

import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.chat.domain.model.AccountSelectionType
import com.aiapp.flowcent.chat.domain.model.ChatHistory
import com.aiapp.flowcent.chat.domain.model.ChatMessage
import com.aiapp.flowcent.core.domain.model.ExpenseItem

data class ChatState(
    val histories: List<ChatHistory> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val answer: String = "",
    val userText: String = "",
    val originalVoiceText: String = "",
    val translatedVoiceText: String = "",
    val isListening: Boolean = false,
    val showSendButton: Boolean = false,
    val isSendingMessage: Boolean = false,
    val uid: String = "",
    val selectionType: AccountSelectionType = AccountSelectionType.PERSONAL,
    val showAccounts: Boolean = false,
    val sharedAccounts: List<Account> = emptyList(),
    val selectedAccountId: String = "",
    val selectedAccountName: String = "",
    val checkedExpenseItems: List<ExpenseItem> = emptyList(),
    val user: User? = null,
    val voiceMessages: List<ChatMessage> = emptyList(),
    val remainingCredits: Int = 0,
    val showSubscriptionSheet: Boolean = false
)

