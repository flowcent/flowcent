package com.aiapp.flowcent.auth.presentation

sealed interface UiEvent {
    data object NavigateToHome : UiEvent
}