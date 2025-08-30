package com.aiapp.flowcent.home.presentation

import kotlinx.datetime.LocalDate

sealed interface UserAction {
    data class SetSelectedDate(val dateString: LocalDate) : UserAction
    data object NavigateToAuth: UserAction
    data object FirebaseSignOut: UserAction
    data object FetchUserUId: UserAction
    data object NavigateToProfile: UserAction
    data object NavigateToInsights: UserAction
}