package com.aiapp.flowcent.home.presentation

import kotlinx.datetime.LocalDate

sealed interface UserAction {
    data object FetchLatestTransactions: UserAction
    data object FetchTotalAmount: UserAction
    data class SetSelectedDate(val dateString: LocalDate) : UserAction
    data object NavigateToAuth: UserAction
    data object FirebaseSignOut: UserAction
    data object FetchUserUId: UserAction
}