package com.aiapp.flowcent.chat.presentation

sealed interface UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent
    object StartAudioPlayer : UiEvent
    object StopAudioPlayer : UiEvent
    object CheckAudioPermission : UiEvent
}