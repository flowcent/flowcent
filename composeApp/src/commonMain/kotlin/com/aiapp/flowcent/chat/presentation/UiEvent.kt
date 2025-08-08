package com.aiapp.flowcent.chat.presentation

sealed interface UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent
    data object StartAudioPlayer : UiEvent
    data object StopAudioPlayer : UiEvent
    data object CheckAudioPermission : UiEvent
}