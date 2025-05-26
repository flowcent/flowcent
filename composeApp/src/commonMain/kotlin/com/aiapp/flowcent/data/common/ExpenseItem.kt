package com.aiapp.flowcent.data.common

import kotlinx.serialization.Serializable

@Serializable
data class ExpenseItem(
    val amount: Int,
    val category: String,
    val title: String
)

