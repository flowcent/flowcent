package com.aiapp.flowcent.accounts.presentation

import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.auth.data.model.User

data class AccountState(
    val isLoading: Boolean = false,
    val accounts: List<Account> = emptyList(),
    val matchingUsers: Set<User>? = null,
    val showSheet: Boolean = false,
    val accountName: String = "",
    val acInitialBalance: String = "",
    val selectedUsers: List<User> = emptyList()
)
