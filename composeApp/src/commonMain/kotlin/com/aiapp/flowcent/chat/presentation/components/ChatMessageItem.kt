package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.domain.model.ChatMessage
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.core.presentation.components.BotContentRow

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    chatState: ChatState,
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    when (message) {
        is ChatMessage.ChatUserMessage -> {
            UserMessage(
                avatarName = chatState.user?.localUserName ?: "Flowcent", text = message.text
            )
        }

        is ChatMessage.ChatBotMessage -> {
            if (message.isLoading) {
                Column(
                    modifier = modifier.fillMaxWidth().padding(top = 24.dp),
                ) {
                    BotContentRow(
                        name = "Marcus",
                        isLoading = true
                    )
                }
            } else if (
                message.isLoadingSave || message.isLoadingDiscard
            ) {
                Column(
                    modifier = modifier.fillMaxWidth().padding(top = 24.dp),
                ) {
                    BotContentRow(
                        name = "Marcus",
                        isLoading = false,
                        isLoadingButton = true
                    )
                }

            } else {
                Column(
                    modifier = modifier.fillMaxWidth().padding(top = 24.dp),
                ) {
                    BotContentRow(
                        name = "Marcus",
                        message = if (message.hasSaved) {
                            "Your spending has been saved"
                        } else if (message.hasDiscarded) {
                            "Your spending has been discarded, please try again"
                        } else {
                            message.text
                        }
                    )

                    if (message.hasSaved.not() && message.hasDiscarded.not()) {
                        Spacer(modifier = Modifier.height(16.dp))

                        ExpenseSection(
                            message = message,
                            chatState = chatState,
                            viewModel = viewModel
                        )
                    }
                }

            }

        }
    }

}