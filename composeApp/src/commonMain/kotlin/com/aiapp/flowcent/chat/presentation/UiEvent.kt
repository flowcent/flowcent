package com.aiapp.flowcent.chat.presentation

import com.aiapp.flowcent.core.utils.DialogType

sealed interface UiEvent {
    data class ShowSnackBar(val message: String) : UiEvent
    data object StartAudioPlayer : UiEvent
    data object StopAudioPlayer : UiEvent
    data object CheckAudioPermission : UiEvent

    data class ShowDialog(
        val dialogType: DialogType,
        val iconRes: Int? = null,
        val title: String = "",
        val body: String = "",
        val extraText: String = "",
        val primaryBtnText: String = "",
        val secondaryBtnText: String = ""
    ) : UiEvent

    data object NavigateToChat : UiEvent
    data object NavigateToBack : UiEvent
}