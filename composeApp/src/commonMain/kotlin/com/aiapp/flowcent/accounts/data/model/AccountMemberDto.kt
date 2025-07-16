package com.aiapp.flowcent.accounts.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountMemberDto(
    val memberId: String? = null,
    val memberUserName: String? = null,
    val memberProfileImage: String? = null,
    val totalAmountAdded: String? = null,
    val totalAmountSpent: String? = null,
)
