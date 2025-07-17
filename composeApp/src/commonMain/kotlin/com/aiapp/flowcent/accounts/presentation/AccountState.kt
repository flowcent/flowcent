package com.aiapp.flowcent.accounts.presentation

import com.aiapp.flowcent.accounts.domain.model.Account

data class AccountState(
    val isLoading: Boolean = false,
    val accounts: List<Account> = emptyList(),
)
