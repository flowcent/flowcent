package com.aiapp.flowcent.accounts.domain

import com.aiapp.flowcent.accounts.data.model.CreateAccountDto
import com.aiapp.flowcent.accounts.data.model.AccountMemberDto
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.accounts.domain.model.AccountMember
import com.aiapp.flowcent.auth.data.model.User


fun AccountMemberDto.toAccountMember(): AccountMember {
    return AccountMember(
        memberId = memberId,
        memberUserName = memberUserName,
        memberProfileImage = memberProfileImage
    )
}

fun AccountMember.toAccountMemberDto(): AccountMemberDto {
    return AccountMemberDto(
        memberId = memberId,
        memberUserName = memberUserName,
        memberProfileImage = memberProfileImage
    )
}

fun CreateAccountDto.toAccount(): Account {
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

fun Account.toAccountDto(): CreateAccountDto {
    return CreateAccountDto(
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

fun User.toAcMemberDto(): AccountMemberDto {
    return AccountMemberDto(
        memberId = uid,
        memberUserName = name,
        memberProfileImage = imageUrl,
    )
}

fun List<CreateAccountDto>.toAccounts(): List<Account> {
    return map { it.toAccount() }
}

fun List<Account>.toAccountDtos(): List<CreateAccountDto> {
    return map { it.toAccountDto() }
}

fun List<User>.toAcMemberDtos(): List<AccountMemberDto> {
    return map { it.toAcMemberDto() }
}
