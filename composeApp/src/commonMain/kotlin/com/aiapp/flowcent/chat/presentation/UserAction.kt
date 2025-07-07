package com.aiapp.flowcent.chat.presentation

import com.aiapp.flowcent.core.domain.model.ExpenseItem

sealed interface UserAction {
    data class SendMessage(val text: String) : UserAction
    data object StartAudioPlayer : UserAction
    data object StopAudioPlayer : UserAction
    data class UpdateText(val text: String) : UserAction
    data class UpdateVoiceText(val originalText: String, val translatedText: String) : UserAction
    object CheckAudioPermission : UserAction
    data class SaveExpenseItemsToDb(val expenseItems: List<ExpenseItem>) : UserAction
    data object DiscardExpenseItems : UserAction
    data object FetchUserUId : UserAction
}