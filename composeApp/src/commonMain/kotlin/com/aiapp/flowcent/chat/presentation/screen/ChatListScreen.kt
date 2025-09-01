package com.aiapp.flowcent.chat.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.domain.model.AccountSelectionType
import com.aiapp.flowcent.chat.domain.model.ChatMessage
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.UserAction
import com.aiapp.flowcent.chat.presentation.components.ChatMessageItem
import com.aiapp.flowcent.core.presentation.components.ToggleSelector


@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier,
    chatState: ChatState,
    viewModel: ChatViewModel,
) {

    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.onAction(UserAction.FetchUserUId)
    }

    LaunchedEffect(key1 = chatState.histories) {
        val lastHistory = chatState.histories.lastOrNull()
        val lastMessage = lastHistory?.messages?.lastOrNull()

        if (lastMessage is ChatMessage.ChatBotMessage && lastMessage.expenseItems.isNotEmpty()) {
            viewModel.onAction(UserAction.UpdateAllCheckedItems(lastMessage.expenseItems))
        }
    }

    LaunchedEffect(chatState.histories) {
        if (chatState.histories.isNotEmpty()) {
            listState.animateScrollToItem(0)
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
                state = listState,
                modifier = Modifier.fillMaxSize(),
                reverseLayout = true,
                contentPadding = PaddingValues(
                    top = 50.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            ) {
                chatState.histories.forEach { history ->
                    itemsIndexed(
                        history.messages.reversed(),
                        key = { _, message -> message.id }) { index, message ->
                        // First item in the reversed list is the newest/topmost prompt
                        val itemModifier = if (index == 0) {
                            Modifier
                                .fillParentMaxHeight() // Fill the screen for the prompt
                                .fillMaxWidth()
                        } else {
                            Modifier.fillMaxWidth()
                        }

                        ChatMessageItem(
                            message = message,
                            chatState = chatState,
                            viewModel = viewModel,
                            modifier = itemModifier
                        )
                    }
                }
            }
        }
    }
}















