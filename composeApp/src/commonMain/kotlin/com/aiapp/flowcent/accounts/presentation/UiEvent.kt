package com.aiapp.flowcent.accounts.presentation

import com.aiapp.flowcent.core.utils.DialogType

sealed interface UiEvent {
    data object ClickAdd : UiEvent
    data object NavigateToAccountDetail : UiEvent
    data object NavigateToChat : UiEvent
    data object NavigateToAccountHome : UiEvent

    data class ShowDialog(
        val dialogType: DialogType,
        val iconRes: Int? = null,
        val title: String = "",
        val body: String = "",
        val extraText: String = "",
        val primaryBtnText: String = "",
        val secondaryBtnText: String = ""
    ) : UiEvent
}