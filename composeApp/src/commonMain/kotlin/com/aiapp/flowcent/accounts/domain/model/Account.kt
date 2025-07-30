package com.aiapp.flowcent.accounts.domain.model

data class Account(
    val createdAt: String = "",
    val createdBy: String = "",
    val initialBalance: String = "0",
    val currentBalance: String = "",
    val accountId: String = "",
    val accountName: String = "",
    val creatorUserId: String = "",
    val creatorUserName: String = "",
    val members: List<AccountMember>? = emptyList(),
    val profileImage: String = "",
    val totalExpense: String = "0",
    val updatedAt: String = "",
    val updatedBy: String = "",
)

///Need to add members after searching
///Accounts that each user is a member of, needs to updated into the User collection
