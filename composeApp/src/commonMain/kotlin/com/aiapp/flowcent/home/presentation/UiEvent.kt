package com.aiapp.flowcent.home.presentation

import com.aiapp.flowcent.core.utils.DialogType

sealed interface UiEvent {
    data class Navigate(val route: String) : UiEvent
    data class ShowSnackbar(val message: String) : UiEvent

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