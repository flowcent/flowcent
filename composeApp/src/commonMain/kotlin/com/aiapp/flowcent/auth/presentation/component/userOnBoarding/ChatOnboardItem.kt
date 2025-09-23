package com.aiapp.flowcent.auth.presentation.component.userOnBoarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.chat.domain.model.ChatMessage
import com.aiapp.flowcent.chat.presentation.components.BotMessageContent
import com.aiapp.flowcent.chat.presentation.components.UserMessage
import com.aiapp.flowcent.core.presentation.components.BotContentRow
import com.aiapp.flowcent.core.presentation.components.ExpenseItemRow
import com.aiapp.flowcent.core.presentation.components.ShimmerChat

@Composable
fun ChatOnboardItem(
    message: ChatMessage,
    state: AuthState,
    modifier: Modifier = Modifier
) {
    when (message) {
        is ChatMessage.ChatUserMessage -> {
            UserMessage(
                avatarName = state.username, text = message.text
            )
        }

        is ChatMessage.ChatBotMessage -> {
            if (message.isLoading) {
                BotMessageContent(
                    avatarName = "Marcus", modifier = modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    ShimmerChat()
                }

            } else {
                Column(
                    modifier = modifier.fillMaxWidth().padding(top = 24.dp),
                ) {
                    BotContentRow(
                        name = "Marcus", message = message.text
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (message.expenseItems.isNotEmpty()) {
                        message.expenseItems.forEach { expenseItem ->
                            ExpenseItemRow(expenseItem = expenseItem)
                        }
                    }
                }
            }

        }
    }

}

