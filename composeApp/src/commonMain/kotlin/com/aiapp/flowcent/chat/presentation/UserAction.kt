package com.aiapp.flowcent.chat.presentation

import com.aiapp.flowcent.chat.domain.model.AccountSelectionType
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.presentation.platform.ConnectivityObserver

sealed interface UserAction {
    data class SendMessage(val text: String) : UserAction
    data object StartAudioPlayer : UserAction
    data object StopAudioPlayer : UserAction
    data class UpdateText(val text: String) : UserAction
    data class UpdateVoiceText(val originalText: String, val translatedText: String) : UserAction
    data object CheckAudioPermission : UserAction
    data object SaveExpenseItemsToDb : UserAction
    data object DiscardExpenseItems : UserAction
    data object FetchUserUId : UserAction
    data class UpdateAccountSelectionType(val selectionType: AccountSelectionType) : UserAction
    data class SelectAccount(val accountDocumentId: String, val accountName: String) : UserAction
    data class UpdateAllCheckedItems(val expenseItems: List<ExpenseItem>) : UserAction
    data class UpdateCheckedItem(val isChecked: Boolean, val expenseItem: ExpenseItem) : UserAction
    data class UpdateListening(val isListening: Boolean) : UserAction
    data class CheckInternet(val status: ConnectivityObserver.Status) : UserAction
}