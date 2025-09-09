package com.aiapp.flowcent.onboarding

sealed interface UserAction {
    data object Next : UserAction
    data object Previous : UserAction
    data object Skip : UserAction
    data object Complete : UserAction
    data class GoTo(val page: Int) : UserAction
}
