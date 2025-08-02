package com.aiapp.flowcent.accounts.data.repository

import com.aiapp.flowcent.accounts.data.model.AccountDto
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.util.Resource

interface AccountRepository {
    suspend fun addAccount(accountDto: AccountDto): Resource<String>
    suspend fun getAccounts(userId: String): Resource<List<Account>>
}