package com.aiapp.flowcent.userOnboarding

sealed interface UserAction {
    data object NavigateToUserOnboard : UserAction
    data object NavigateToChatOnboard : UserAction
    data class UpdateText(val text: String) : UserAction
    data class SendMessage(val text: String) : UserAction
    data object ResetAllState : UserAction
}