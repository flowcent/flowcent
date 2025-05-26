package com.aiapp.flowcent.chat.presentation

sealed interface UserAction {
    data class SendMessage(val text: String) : UserAction
    data object StartRecording : UserAction
    data object StopRecording : UserAction
    data object CancelRecording : UserAction
    data class UpdateText(val text: String) : UserAction
    data class UpdateVoiceText(val originalText: String, val translatedText: String) : UserAction
}