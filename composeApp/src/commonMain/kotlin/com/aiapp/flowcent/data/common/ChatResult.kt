package com.aiapp.flowcent.data.common

import kotlinx.serialization.Serializable

@Serializable
data class ChatResult(
    val answer: String,
    val data: List<ExpenseItem>,
)

