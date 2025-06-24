package com.aiapp.flowcent.chat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatResult(
    val answer: String,
    val data: List<ExpenseItem>,
)

