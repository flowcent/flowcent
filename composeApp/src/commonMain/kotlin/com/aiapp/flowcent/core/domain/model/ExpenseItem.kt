package com.aiapp.flowcent.core.domain.model

import com.aiapp.flowcent.core.domain.utils.EnumConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExpenseItem(
    val amount: Double,
    val category: String,
    val title: String,
    val type: EnumConstants.TransactionType,
    @SerialName("involved_party") val involvedParty: String = ""
)

