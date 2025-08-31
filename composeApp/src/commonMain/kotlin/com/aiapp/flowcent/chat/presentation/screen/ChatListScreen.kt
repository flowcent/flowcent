package com.aiapp.flowcent.chat.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.domain.model.AccountSelectionType
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.UserAction
import com.aiapp.flowcent.chat.presentation.components.AccountSelectorRow
import com.aiapp.flowcent.chat.presentation.components.BotMessageContent
import com.aiapp.flowcent.chat.presentation.components.PromptSave
import com.aiapp.flowcent.chat.presentation.components.SpendingList
import com.aiapp.flowcent.chat.presentation.components.UserMessage
import com.aiapp.flowcent.core.presentation.components.ShimmerChat
import com.aiapp.flowcent.core.presentation.components.ToggleSelector


@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier,
    chatState: ChatState,
    viewModel: ChatViewModel,
) {

    LaunchedEffect(Unit) {
        viewModel.onAction(UserAction.FetchUserUId)
    }

    LaunchedEffect(key1 = chatState.messages) {
        if (chatState.messages.isNotEmpty() && chatState.messages.last().isUser.not()) {
            viewModel.onAction(UserAction.UpdateAllCheckedItems(chatState.messages.last().expenseItems))
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ToggleSelector(
                options = listOf(
                    AccountSelectionType.PERSONAL to "PERSONAL",
                    AccountSelectionType.SHARED to "SHARED"
                ),
                selectedOption = chatState.selectionType,
                onOptionSelected = {
                    viewModel.onAction(UserAction.UpdateAccountSelectionType(it))
                },
                backgroundColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
                reverseLayout = true
            ) {
                item {
                    Spacer(Modifier.height(24.dp))
                }
                item {
                    if (chatState.messages.isEmpty()) {
                        BotMessageContent(
                            avatarName = "Marcus",
                            modifier = Modifier.fillMaxWidth()
                                .height(150.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = "Hi, I'm Marcus. I can help you record financial transactions. Please phrase your questions like this: I just paid $$800 house rent, Just got my salary which is $$8000. Add it, Had dinner that cost $$16.97",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 14
                                )
                            }
                        }
                    }
                }

                items(chatState.messages.reversed()) { message ->
                    if (message.isUser) {
                        UserMessage(
                            avatarName = chatState.user?.localUserName ?: "Flowcent",
                            text = message.text
                        )
                    } else {
                        Column(
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            if (message.isBotMessageLoading) {
                                BotMessageContent(
                                    avatarName = "Marcus",
                                    modifier = Modifier.fillMaxWidth()
                                        .height(150.dp)
                                ) {
                                    ShimmerChat(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                    )
                                }

                            } else {
                                BotMessageContent(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth()
                                            .background(
                                                MaterialTheme.colorScheme.surfaceVariant,
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = message.text,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            style = MaterialTheme.typography.bodyMedium
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))
                                        if (message.expenseItems.isNotEmpty()) {
                                            SpendingList(
                                                expenseItems = message.expenseItems,
                                                checkedExpenseItems = chatState.checkedExpenseItems,
                                                onCheckedItem = { isChecked, expenseItem ->
                                                    viewModel.onAction(
                                                        UserAction.UpdateCheckedItem(
                                                            isChecked,
                                                            expenseItem
                                                        )
                                                    )
                                                },
                                                onEdit = {
                                                    viewModel.onAction(
                                                        UserAction.EditExpenseItem(it)
                                                    )
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

                                            if (chatState.selectionType == AccountSelectionType.SHARED &&
                                                chatState.sharedAccounts.isNotEmpty()
                                            ) {
                                                AccountSelectorRow(
                                                    accounts = chatState.sharedAccounts,
                                                    selectedAccountId = chatState.selectedAccountId,
                                                    onAccountSelected = { account ->
                                                        viewModel.onAction(
                                                            UserAction.SelectAccount(
                                                                account.id,
                                                                account.accountName
                                                            )
                                                        )
                                                    }
                                                )
                                            }

                                            PromptSave(
                                                selectionType = chatState.selectionType,
                                                selectedAccountName = chatState.selectedAccountName,
                                                onClickSave = {
                                                    viewModel.onAction(
                                                        UserAction.SaveExpenseItemsToDb
                                                    )
                                                },
                                                onClickDiscard = {
                                                    viewModel.onAction(UserAction.DiscardExpenseItems)
                                                }
                                            )


                                        }
                                    }
                                }
                                Spacer(Modifier.height(24.dp))
                            }
                        }
                    }
                }

            }
        }
    }

}





