package com.aiapp.flowcent.chat.presentation

sealed interface UserAction {
    data class SendMessage(val text: String) : UserAction
    data object StartAudioPlayer : UserAction
    data object StopAudioPlayer : UserAction
    data class UpdateText(val text: String) : UserAction
    data class UpdateVoiceText(val originalText: String, val translatedText: String) : UserAction
    object CheckAudioPermission : UserAction
}