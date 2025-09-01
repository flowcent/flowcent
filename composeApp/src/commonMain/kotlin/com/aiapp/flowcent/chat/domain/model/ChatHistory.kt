package com.aiapp.flowcent.chat.domain.model

data class ChatHistory(
    val id: String = "",
    val messages: List<ChatMessage> = emptyList()
)

