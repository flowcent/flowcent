package com.aiapp.flowcent.accounts.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateAccountDto(
    val createdAt: String = "",
    val createdBy: String = "",
    val initialBalance: String = "",
    val currentBalance: String = "",
    val accountId: String = "",
    val accountName: String = "",
    val creatorUserId: String = "",
    val members: List<AccountMemberDto>? = emptyList(),
    val memberIds: List<String>? = emptyList(),
    val profileImage: String = "",
    val totalExpense: String = "0",
    val updatedAt: String = "",
    val updatedBy: String = ""
)