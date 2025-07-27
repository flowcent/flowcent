package com.aiapp.flowcent.accounts.data.model

import kotlinx.serialization.Serializable

@Serializable
data class CreateAccountDto(
    val createdAt: String? = null,
    val createdBy: String? = null,
    val initialBalance: String? = null,
    val currentBalance: String? = null,
    val accountId: String? = null,
    val accountName: String? = null,
    val creatorUserId: String? = null,
    val creatorUserName: String? = null,
    val members: List<AccountMemberDto>? = null,
    val profileImage: String? = null,
    val totalExpense: String? = null,
    val updatedAt: String? = null,
    val updatedBy: String? = null
)