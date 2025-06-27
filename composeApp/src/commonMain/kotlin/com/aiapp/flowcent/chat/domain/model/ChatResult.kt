package com.aiapp.flowcent.chat.domain.model

import com.aiapp.flowcent.data.request.ExpenseItem
import kotlinx.serialization.Serializable

@Serializable
data class ChatResult(
    val answer: String,
    val data: List<ExpenseItem>,
)

