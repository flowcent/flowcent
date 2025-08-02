package com.aiapp.flowcent.accounts.domain

import com.aiapp.flowcent.accounts.data.model.AccountDto
import com.aiapp.flowcent.accounts.data.model.AccountMemberDto
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.accounts.domain.model.AccountMember
import com.aiapp.flowcent.auth.data.model.User


fun AccountMemberDto.toAccountMember(): AccountMember {
    return AccountMember(
        memberId = memberId,
        memberUserName = memberUserName,
        memberProfileImage = memberProfileImage,
        totalContribution = totalContribution,
        totalExpense = totalExpense
    )
}

fun AccountMember.toAccountMemberDto(): AccountMemberDto {
    return AccountMemberDto(
        memberId = memberId,
        memberUserName = memberUserName,
        memberProfileImage = memberProfileImage,
        totalContribution = totalContribution,
        totalExpense = totalExpense
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
        creatorUserId = creatorUserId,
        members = members?.map { it.toAccountMember() },
        profileImage = profileImage,
        totalExpense = totalExpense,
        updatedAt = updatedAt,
        updatedBy = updatedBy
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
        creatorUserId = creatorUserId,
        members = members?.map { it.toAccountMemberDto() },
        profileImage = profileImage,
        totalExpense = totalExpense,
        updatedAt = updatedAt,
        updatedBy = updatedBy,
        totalAddition = totalAddition
    )
}

fun User.toAcMemberDto(): AccountMemberDto {
    return AccountMemberDto(
        memberId = uid,
        memberUserName = name,
        memberProfileImage = imageUrl,
    )
}


fun List<AccountDto>.toAccounts(): List<Account> {
    return map { it.toAccount() }
}

fun List<Account>.toAccountDtos(): List<AccountDto> {
    return map { it.toAccountDto() }
}

fun List<User>.toAcMemberDtos(): List<AccountMemberDto> {
    return map { it.toAcMemberDto() }
}

fun List<User>.toMemberIds(): List<String> {
    return map { it.toAcMemberDto().memberId.toString() }
}
