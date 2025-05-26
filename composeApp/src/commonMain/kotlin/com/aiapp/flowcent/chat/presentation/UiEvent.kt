package com.aiapp.flowcent.chat.presentation

sealed interface UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent
}