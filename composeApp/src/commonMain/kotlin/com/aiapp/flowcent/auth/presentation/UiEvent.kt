package com.aiapp.flowcent.auth.presentation

import com.aiapp.flowcent.core.utils.DialogType

sealed interface UiEvent {
    data object NavigateToHome : UiEvent
    data object NavigateToBasicIntro : UiEvent
    data object NavigateToSignIn : UiEvent
    data object NavigateToSignUp : UiEvent

    data class ShowDialog(
        val dialogType: DialogType,
        val iconRes: Int? = null,
        val title: String = "",
        val body: String = "",
        val extraText: String = "",
        val primaryBtnText: String = "",
        val secondaryBtnText: String = ""
    ) : UiEvent

    data object HideDialog : UiEvent
}