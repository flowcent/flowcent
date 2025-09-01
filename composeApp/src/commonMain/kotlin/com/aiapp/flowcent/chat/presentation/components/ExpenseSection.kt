package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.domain.model.AccountSelectionType
import com.aiapp.flowcent.chat.domain.model.ChatMessage
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.UserAction

@Composable
fun ExpenseSection(
    message: ChatMessage.ChatBotMessage,
    chatState: ChatState,
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    if (message.expenseItems.isNotEmpty()) {
        Column(modifier = modifier) {
            // Spending list
            SpendingList(
                expenseItems = message.expenseItems,
                checkedExpenseItems = chatState.checkedExpenseItems,
                onCheckedItem = { isChecked, expenseItem ->
                    viewModel.onAction(UserAction.UpdateCheckedItem(isChecked, expenseItem))
                },
                onEdit = { expenseItem ->
                    viewModel.onAction(UserAction.EditExpenseItem(expenseItem))
                },
                onDelete = { expenseItem ->
                    viewModel.onAction(
                        UserAction.DeleteExpenseItem(
                            expenseItem = expenseItem,
                            messageId = message.id
                        )
                    )
                },
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // Account selector for shared accounts
            if (chatState.selectionType == AccountSelectionType.SHARED &&
                chatState.sharedAccounts.isNotEmpty()
            ) {
                AccountSelectorRow(
                    accounts = chatState.sharedAccounts,
                    selectedAccountId = chatState.selectedAccountId,
                    onAccountSelected = { account ->
                        viewModel.onAction(
                            UserAction.SelectAccount(account.id, account.accountName)
                        )
                    }
                )
            }

            // Prompt save/discard
            PromptSave(
                selectionType = chatState.selectionType,
                selectedAccountName = chatState.selectedAccountName,
                onClickSave = { viewModel.onAction(UserAction.SaveExpenseItemsToDb) },
                onClickDiscard = { viewModel.onAction(UserAction.DiscardExpenseItems) }
            )
        }
    }
}