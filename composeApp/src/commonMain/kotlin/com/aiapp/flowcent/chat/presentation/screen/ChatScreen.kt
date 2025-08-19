package com.aiapp.flowcent.chat.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.domain.model.AccountSelectionType
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.UserAction
import com.aiapp.flowcent.chat.presentation.components.AccountSelectorRow
import com.aiapp.flowcent.chat.presentation.components.AccountTypeSelectionToggle
import com.aiapp.flowcent.chat.presentation.components.BotMessage
import com.aiapp.flowcent.chat.presentation.components.ChatInput
import com.aiapp.flowcent.chat.presentation.components.PromptSave
import com.aiapp.flowcent.chat.presentation.components.SpendingList
import com.aiapp.flowcent.chat.presentation.components.UserMessage
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.outline_charger
import org.jetbrains.compose.resources.painterResource


@Composable
fun ChatScreen(
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
                .fillMaxSize()
                .padding(16.dp)
        ) {
            AccountTypeSelectionToggle(
                selectionType = chatState.selectionType,
                onSelectionChanged = {
                    viewModel.onAction(UserAction.UpdateAccountSelectionType(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
            )

            if (chatState.messages.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Start chatting to manage your expenses!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(vertical = 24.dp),
                    reverseLayout = true
                ) {
                    println("Sohan messages: ${chatState.messages}")
                    items(chatState.messages.reversed()) {
                        if (it.isUser) {
                            UserMessage(text = it.text)
                        } else {
                            Column {
                                if (it.isBotMessageLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.CenterHorizontally),
                                        color = Color.Blue
                                    )
                                } else {
                                    Row(
                                        modifier = Modifier.padding(8.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(Res.drawable.outline_charger),
                                            contentDescription = null,
                                            tint = Color.Black,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))

                                        Column(
                                            modifier = Modifier
                                                .background(
                                                    Color.White,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .padding(16.dp)
                                        ) {
                                            BotMessage(text = it.text, isRich = true)
                                            if (it.expenseItems.isNotEmpty()) {
                                                SpendingList(
                                                    expenseItems = it.expenseItems,
                                                    checkedExpenseItems = chatState.checkedExpenseItems,
                                                    onCheckedItem = { isChecked, expenseItem ->
                                                        viewModel.onAction(
                                                            UserAction.UpdateCheckedItem(
                                                                isChecked,
                                                                expenseItem
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
                                }
                            }
                        }
                    }
                }
            }
        }

        ChatInput(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            state = chatState,
            isListening = chatState.isListening,
            onUpdateText = {
                viewModel.onAction(UserAction.UpdateText(it))
            },
            onClickMic = {
                viewModel.onAction(UserAction.NavigateToVoiceScreen)
            }
        ) {
            viewModel.onAction(UserAction.SendMessage(chatState.userText))
        }
    }

}





