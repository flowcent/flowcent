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
fun BotMessageWithExpenses(
    message: ChatMessage.ChatBotMessage,
    chatState: ChatState,
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(top = 24.dp),
    ) {
        BotContentRow(
            name = "Marcus", message = message.text
        )

        Spacer(modifier = Modifier.height(24.dp))

        ExpenseSection(
            message = message,
            chatState = chatState,
            viewModel = viewModel
        )
    }
}