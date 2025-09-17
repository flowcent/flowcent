package com.aiapp.flowcent.userOnboarding

sealed interface UiEvent {
    data class Navigate(val route: String) : UiEvent
}