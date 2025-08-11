package com.aiapp.flowcent.accounts.domain.model

data class AccountMember(
    val memberId: String = "",
    val memberFullName: String = "",
    val memberLocalUserName: String = "",
    val memberProfileImage: String = "",
    val totalContribution: Double = 0.0,
    val totalExpense: Double = 0.0,
)
