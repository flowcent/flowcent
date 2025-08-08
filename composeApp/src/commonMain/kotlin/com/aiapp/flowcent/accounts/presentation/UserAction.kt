package com.aiapp.flowcent.accounts.presentation

import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.core.data.model.TransactionDto

sealed interface UserAction {
    data object ClickAdd : UserAction
    data object FetchRegisteredPhoneNumbers : UserAction
    data class UpdateSheetState(val sheetState: Boolean) : UserAction
    data class UpdateAccountName(val accountName: String) : UserAction
    data class UpdateAcInitialBalance(val initialBalance: Double) : UserAction
    data class OnUserCheckedChange(val user: User, val checked: Boolean) : UserAction
    data class OnRemoveUser(val user: User) : UserAction
    data object CreateAccount : UserAction
    data object FetchUserUId : UserAction
    data class OnAccountItemClick(val account: Account) : UserAction
    data object AddTransactionToAccount : UserAction
}