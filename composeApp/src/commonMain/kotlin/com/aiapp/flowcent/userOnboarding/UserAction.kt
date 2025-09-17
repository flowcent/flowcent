package com.aiapp.flowcent.userOnboarding

sealed interface UserAction {
    data object NavigateToUserOnboard : UserAction
}