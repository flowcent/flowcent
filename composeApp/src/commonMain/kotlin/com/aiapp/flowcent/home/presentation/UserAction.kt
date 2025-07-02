package com.aiapp.flowcent.home.presentation

import kotlinx.datetime.LocalDate

sealed interface UserAction {
    data object FetchLatestTransactions: UserAction
    data class SetSelectedDate(val dateString: LocalDate) : UserAction
}