package com.aiapp.flowcent.accounts.domain.utils

import com.aiapp.flowcent.accounts.data.model.AccountDto
import com.aiapp.flowcent.accounts.data.model.AccountMemberDto
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.accounts.domain.model.AccountMember
import com.aiapp.flowcent.accounts.domain.model.MemberRole
import com.aiapp.flowcent.auth.data.model.User


fun AccountMemberDto.toAccountMember(): AccountMember {
    return AccountMember(
        flowCentUserId = flowCentUserId,
        memberId = memberId,
        memberFullName = memberFullName,
        memberLocalUserName = memberLocalUserName,
        memberProfileImage = memberProfileImage,
        totalContribution = totalContribution,
        totalExpense = totalExpense,
        role = MemberRole.fromDisplayName(role)
    )
}

fun AccountMember.toAccountMemberDto(): AccountMemberDto {
    return AccountMemberDto(
        flowCentUserId = flowCentUserId,
        memberId = memberId,
        memberFullName = memberFullName,
        memberLocalUserName = memberLocalUserName,
        memberProfileImage = memberProfileImage,
        totalContribution = totalContribution,
        totalExpense = totalExpense,
        role = role.displayName
    )
}

fun AccountDto.toAccount(): Account {
    return Account(
        id = id,
        createdAt = createdAt,
        createdBy = createdBy,
        initialBalance = initialBalance,
        currentBalance = currentBalance,
        accountId = accountId,
        accountName = accountName,
        accountDescription = accountDescription,
        creatorUserId = creatorUserId,
        members = members?.map { it.toAccountMember() },
        profileImage = profileImage,
        totalExpense = totalExpense,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        accountIconId = accountIconId
    )
}

fun Account.toAccountDto(): AccountDto {
    return AccountDto(
        createdAt = createdAt,
        createdBy = createdBy,
        initialBalance = initialBalance,
        currentBalance = currentBalance,
        accountId = accountId,
        accountName = accountName,
        accountDescription = accountDescription,
        creatorUserId = creatorUserId,
        members = members?.map { it.toAccountMemberDto() },
        profileImage = profileImage,
        totalExpense = totalExpense,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        totalAddition = totalAddition,
        accountIconId = accountIconId
    )
}

fun User.toAcMemberDto(
    acCreatorUid: String
): AccountMemberDto {
    val role = if (uid == acCreatorUid) MemberRole.ADMIN else MemberRole.MEMBER

    return AccountMemberDto(
        flowCentUserId = flowCentUserId,
        memberId = uid,
        memberFullName = fullName,
        memberLocalUserName = localUserName,
        memberProfileImage = imageUrl,
        role = role.displayName
    )
}


fun List<AccountDto>.toAccounts(): List<Account> {
    return map { it.toAccount() }
}

fun List<Account>.toAccountDtos(): List<AccountDto> {
    return map { it.toAccountDto() }
}

fun List<User>.toAcMemberDtos(
    acCreatorUid: String
): List<AccountMemberDto> {
    return map { it.toAcMemberDto(acCreatorUid) }
}

fun List<User>.toMemberIds(
    acCreatorUid: String
): List<String> {
    return map { it.toAcMemberDto(acCreatorUid).memberId }
}
