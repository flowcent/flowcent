package com.aiapp.flowcent.accounts.domain.model

data class Account(
    val id: String = "",
    val createdAt: Long = 0L,
    val createdBy: String = "",
    val initialBalance: Double = 0.0,
    val currentBalance: String = "",
    val accountId: String = "",
    val accountName: String = "",
    val creatorUserId: String = "",
    val creatorUserName: String = "",
    val members: List<AccountMember>? = emptyList(),
    val profileImage: String = "",
    val totalExpense: Double = 0.0,
    val totalAddition: Double = 0.0,
    val updatedAt: Long = 0L,
    val updatedBy: String = "",
    val iconId: Int = 1111,
)