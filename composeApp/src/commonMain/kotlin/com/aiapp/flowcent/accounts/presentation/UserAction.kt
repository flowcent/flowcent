package com.aiapp.flowcent.accounts.presentation

import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.accounts.domain.model.AccountDurationType
import com.aiapp.flowcent.auth.data.model.User
import kotlinx.datetime.LocalDate

sealed interface UserAction {
    data object ClickAdd : UserAction
    data object FetchRegisteredPhoneNumbers : UserAction
    data class UpdateSheetState(val sheetState: Boolean) : UserAction
    data class UpdateDatePickerSheerState(val sheetState: Boolean) : UserAction
    data class UpdateAccountName(val accountName: String) : UserAction
    data class UpdateAcInitialBalance(val initialBalance: Double) : UserAction
    data class OnUserCheckedChange(val user: User, val checked: Boolean) : UserAction
    data class OnRemoveMember(val user: User) : UserAction
    data object CreateAccount : UserAction
    data object FetchUserUId : UserAction
    data class OnAccountItemClick(val account: Account) : UserAction
    data object AddTransactionToAccount : UserAction
    data object GetDailyTransactions : UserAction
    data object NavigateToChat : UserAction
    data class SetSelectedDate(val dateString: LocalDate) : UserAction
    data class GetUsersDailyTransaction(val uid: String) : UserAction
    data class UpdateAccountDescription(val accountDescription: String) : UserAction
    data class SelectAccountIcon(val id: Int) : UserAction
    data class UpdateAccountDurationType(val accountDurationType: AccountDurationType) : UserAction
}