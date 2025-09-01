package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.domain.model.ChatMessage
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.core.presentation.components.ShimmerChat

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
                BotMessageContent(
                    avatarName = "Marcus", modifier = modifier.fillMaxWidth()
                ) {
                    ShimmerChat()
                }

            } else {
                BotMessageContent(
                    avatarName = "Marcus", modifier = modifier.fillMaxWidth()
                ) {
                    BotMessageWithExpenses(
                        message = message,
                        chatState = chatState,
                        viewModel = viewModel,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

        }
    }

}