package com.aiapp.flowcent.core.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseItemDto(
    val amount: Double,
    val category: String,
    val title: String,
    val type: String,
    @SerialName("involved_party") val involvedParty: String = ""
)
