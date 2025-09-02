package com.aiapp.flowcent.accounts.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountMemberDto(
    val flowCentUserId: String = "",
    val memberId: String = "",
    val memberFullName: String = "",
    val memberLocalUserName: String = "",
    val memberProfileImage: String = "",
    val totalContribution: Double = 0.0,
    val totalExpense: Double = 0.0,
    val role: String = ""
)
