package com.aiapp.flowcent.home.presentation

sealed interface UiEvent {
    data class Navigate(val route: String) : UiEvent
    data class ShowSnackbar(val message: String) : UiEvent
}