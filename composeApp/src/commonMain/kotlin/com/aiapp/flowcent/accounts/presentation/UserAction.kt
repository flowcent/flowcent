package com.aiapp.flowcent.accounts.presentation

import com.aiapp.flowcent.accounts.domain.model.Account

sealed interface UserAction {
    data class AddAccount(val account: Account) : UserAction
}