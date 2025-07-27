package com.aiapp.flowcent.accounts.data.repository

import com.aiapp.flowcent.accounts.data.model.CreateAccountDto
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.util.Resource

interface AccountRepository {
    suspend fun addAccount(createAccountDto: CreateAccountDto): Resource<String>
    suspend fun getAccounts(userId: String): Resource<List<Account>>
}