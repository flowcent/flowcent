package com.aiapp.flowcent.accounts.presentation

sealed interface UiEvent {
    data object ClickAdd: UiEvent
}