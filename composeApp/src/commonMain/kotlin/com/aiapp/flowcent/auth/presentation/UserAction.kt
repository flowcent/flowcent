package com.aiapp.flowcent.auth.presentation

sealed interface UserAction {
    data object NavigateToHome : UserAction
    data object FirebaseSignOut: UserAction
}