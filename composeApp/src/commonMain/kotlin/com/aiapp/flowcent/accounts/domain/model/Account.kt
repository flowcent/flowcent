package com.aiapp.flowcent.accounts.domain.model

data class Account(
    val createdAt: String? = null,
    val createdBy: String? = null,
    val initialBalance: String? = null,
    val currentBalance: String? = null,
    val accountId: String? = null,
    val accountName: String? = null,
    val creatorUserId: String? = null,
    val creatorUserName: String? = null,
    val members: List<AccountMember>? = null,
    val profileImage: String? = null,
    val totalExpense: String? = null,
    val updatedAt: String? = null,
    val updatedBy: String? = null
)

///Need to add members after searching
///Accounts that each user is a member of, needs to updated into the User collection
