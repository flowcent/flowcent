package com.aiapp.flowcent.home.presentation

sealed interface UiEvent {
    data class ShowSnackbar(val message: String) : UiEvent
    data object NavigateToAuth : UiEvent
}