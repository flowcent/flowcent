package com.aiapp.flowcent.chat.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ExpenseItem(
    val amount: Int,
    val category: String,
    val title: String
)

