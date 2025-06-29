package com.aiapp.flowcent.core.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseItem(
    val amount: Int,
    val category: String,
    val title: String,
    val type: String,
    @SerialName("involved_party") val involvedParty: String = ""
)

