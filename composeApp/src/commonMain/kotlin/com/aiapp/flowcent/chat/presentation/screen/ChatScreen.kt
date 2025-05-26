package com.aiapp.flowcent.chat.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.UserAction
import com.aiapp.flowcent.data.common.ExpenseItem
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.compose_multiplatform
import flowcent.composeapp.generated.resources.mike
import org.jetbrains.compose.resources.painterResource


@Composable
fun ChatScreen(
    chatState: ChatState,
    viewModel: ChatViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000))
            .padding(16.dp)
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true
        ) {
            items(chatState.messages.reversed()) {
                if (it.isUser) {
                    UserMessage(text = it.text)
                } else {
                    if (chatState.isCircularLoading && chatState.messages[chatState.messages.lastIndex].isUser.not()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            color = Color(0xFFFFFFFF)
                        )
                    } else {
                        Column {
                            BotMessage(text = it.text)
                            if (it.expenseItems.isNotEmpty()) {
                                it.expenseItems.forEach { expenseItem ->
                                    SpendingCard(expenseItem)
                                }
                            }
                        }
                    }
                }
            }
        }

        ChatInput(
            state = chatState,
            onClickSend = {
                viewModel.onAction(UserAction.SendMessage(chatState.userText))
            },
            onClickMic = {
                viewModel.onAction(UserAction.StartRecording)
            }
        ) {
            viewModel.onAction(UserAction.UpdateText(it))
        }
    }
}


@Composable
fun BotMessage(text: String, isRich: Boolean = false) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(
            painter = painterResource(Res.drawable.compose_multiplatform),
            contentDescription = null,
            tint = Color(0xFFFFFFFF),
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            shape = RoundedCornerShape(12.dp), color = Color(0xFF1E1E1E)
        ) {
            Text(
                text = text,
                color = Color(0xFFFFFFFF),
                modifier = Modifier.padding(12.dp),
                style = if (isRich) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun UserMessage(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp), color = Color(0xFF6C4DFF) // Purple-ish
        ) {
            Text(
                text = text,
                color = Color(0xFFFFFFFF),
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun SpendingCard(expenseItem: ExpenseItem) {
    Card(
        backgroundColor = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    expenseItem.title,
                    color = Color(0xFFFFFFFF),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    expenseItem.category,
                    color = Color(0xFF808080),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                expenseItem.amount.toString(),
                color = Color(0xFFFFFFFF),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun ChatInput(
    state: ChatState,
    onClickSend: () -> Unit = {},
    onClickMic: () -> Unit = {},
    onUpdateText: (text: String) -> Unit = {}
) {

    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .background(Color(0xFF000000))
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onClickMic() }) {
            Icon(painterResource(Res.drawable.compose_multiplatform), contentDescription = "Mic", tint = Color(0xFFFFFFFF))
        }
        TextField(
            value = state.userText,
            onValueChange = { newValue ->
                onUpdateText(newValue)
            },
            placeholder = { Text("Type your messenger here", color = Color(0xFF808080)) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E1E1E),
                focusedTextColor = Color(0xFFFFFFFF),
                cursorColor = Color(0xFFFFFFFF)
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp)
        )
        IconButton(onClick = {
            onClickSend()
        }) {
            Icon(Icons.Default.Send, contentDescription = "Send", tint = Color(0xFFFFFFFF))
        }
    }
}