package com.aiapp.flowcent.chat.domain.model

import com.aiapp.flowcent.core.domain.model.ExpenseItem

sealed class ChatMessage {
    abstract val id: String

    data class ChatUserMessage(
        override val id: String,
        val text: String
    ) : ChatMessage()

    data class ChatBotMessage(
        override val id: String,
        val text: String,
        val expenseItems: List<ExpenseItem> = emptyList(),
        val isLoading: Boolean = false,
        val isLoadingSave: Boolean = false,
        val isLoadingDiscard: Boolean = false,
        val hasSaved: Boolean = false,
        val hasDiscarded: Boolean = false,
    ) : ChatMessage()
}
