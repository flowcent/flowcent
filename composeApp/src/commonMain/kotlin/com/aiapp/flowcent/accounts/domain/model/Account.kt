package com.aiapp.flowcent.accounts.domain.model

data class Account(
    val id: String = "",
    val createdAt: String = "",
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
    val updatedAt: String = "",
    val updatedBy: String = "",
)

///Need to add members after searching
///Accounts that each user is a member of, needs to updated into the User collection
