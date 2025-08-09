package com.aiapp.flowcent.accounts.data.repository

import com.aiapp.flowcent.accounts.data.model.AccountDto
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.core.domain.utils.Resource
import com.aiapp.flowcent.core.data.model.TransactionDto

interface AccountRepository {
    suspend fun addAccount(accountDto: AccountDto): Resource<String>
    suspend fun getAccounts(userId: String): Resource<List<Account>>
    suspend fun addAccountTransaction(
        accountDocumentId: String,
        transactionDto: TransactionDto
    ): Resource<String>

    suspend fun getDailyAccountTransactions(accountDocumentId: String, dateString: String): Resource<List<TransactionDto>>
}