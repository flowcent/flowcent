package com.aiapp.flowcent.core.domain.model

import com.aiapp.flowcent.core.domain.utils.EnumConstants
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Serializable
data class ExpenseItem(
    val id: String = Uuid.random().toString(),
    val amount: Double,
    val category: String,
    val title: String,
    val type: EnumConstants.TransactionType,
    @SerialName("involved_party") val involvedParty: String = ""
)

