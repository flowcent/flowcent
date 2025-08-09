package com.aiapp.flowcent.accounts.presentation

import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.domain.model.Transaction

data class AccountState(
    val isLoading: Boolean = false,
    val accounts: List<Account> = emptyList(),
    val matchingUsers: Set<User>? = null,
    val showSheet: Boolean = false,
    val accountName: String = "",
    val acInitialBalance: Double = 0.0,
    val selectedUsers: List<User> = emptyList(),
    val uid: String = "",
    val selectedAccount: Account? = null,
    val latestTransactions: List<Transaction> = emptyList(),
    val selectedDate: String? = null,
)
