package com.aiapp.flowcent.accounts.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateAccountDto(
    val createdAt: String = "",
    val createdBy: String = "",
    val initialBalance: Double = 0.0,
    val currentBalance: String = "",
    val accountId: String = "",
    val accountName: String = "",
    val creatorUserId: String = "",
    val members: List<AccountMemberDto>? = emptyList(),
    val memberIds: List<String>? = emptyList(),
    val profileImage: String = "",
    val totalExpense: Double = 0.0,
    val totalAddition: Double = 0.0,
    val updatedAt: String = "",
    val updatedBy: String = "",
)