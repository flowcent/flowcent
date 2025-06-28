package com.aiapp.flowcent.data.request

import kotlinx.serialization.Serializable

@Serializable
data class ExpenseItem(
    val amount: Int,
    val category: String,
    val title: String,
    val type: String,
    val involved_party: String = "",
)

