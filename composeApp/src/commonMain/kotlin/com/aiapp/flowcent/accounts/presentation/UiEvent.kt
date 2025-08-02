package com.aiapp.flowcent.accounts.presentation

sealed interface UiEvent {
    data object ClickAdd : UiEvent
    data object NavigateToAccountDetail : UiEvent
}