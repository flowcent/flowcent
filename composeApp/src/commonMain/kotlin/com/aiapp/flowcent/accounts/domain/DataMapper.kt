package com.aiapp.flowcent.accounts.domain

import com.aiapp.flowcent.accounts.data.model.AccountDto
import com.aiapp.flowcent.accounts.data.model.AccountMemberDto
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.accounts.domain.model.AccountMember


fun AccountMemberDto.toAccountMember(): AccountMember {
    return AccountMember(
        memberId = memberId,
        memberUserName = memberUserName,
        memberProfileImage = memberProfileImage,
        totalAmountAdded = totalAmountAdded,
        totalAmountSpent = totalAmountSpent
    )
}

fun AccountMember.toAccountMemberDto(): AccountMemberDto {
    return AccountMemberDto(
        memberId = memberId,
        memberUserName = memberUserName,
        memberProfileImage = memberProfileImage,
        totalAmountAdded = totalAmountAdded,
        totalAmountSpent = totalAmountSpent
    )
}

fun AccountDto.toAccount(): Account {
    return Account(
        createdAt = createdAt,
        createdBy = createdBy,
        initialBalance = initialBalance,
        currentBalance = currentBalance,
        accountId = accountId,
        accountName = accountName,
        creatorUserId = creatorUserId,
        creatorUserName = creatorUserName,
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
        creatorUserName = creatorUserName,
        members = members?.map { it.toAccountMemberDto() },
        profileImage = profileImage,
        totalExpense = totalExpense,
        updatedAt = updatedAt,
        updatedBy = updatedBy
    )
}

fun List<AccountDto>.toAccounts(): List<Account> {
    return map { it.toAccount() }
}

fun List<Account>.toAccountDtos(): List<AccountDto> {
    return map { it.toAccountDto() }
}