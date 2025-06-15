package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.theming.AppTheme
import com.aiapp.flowcent.theming.Colors

@Composable
fun ChatInput(
    state: ChatState,
    isListening: Boolean = false,
    onUpdateText: (text: String) -> Unit = {},
    onClickMic: () -> Unit = {},
    onClickSend: () -> Unit = {},
) {

    AppTheme {
        Row(
            modifier = Modifier
                .height(70.dp)
                .padding(vertical = 8.dp, horizontal = 8.dp)
                .background(Colors.White)
                .border(1.dp, Colors.Black, shape = MaterialTheme.shapes.medium)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onClickMic() }) {
                if(isListening){
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Colors.Black)
                }else{
                    Icon(Icons.Default.Mic, contentDescription = "Mic", tint = Colors.Black)
                }
            }
            TextField(
                value = state.userText,
                onValueChange = { newValue ->
                    onUpdateText(newValue)
                },
                placeholder = { Text("Type your messenger here") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Colors.Black,
                    cursorColor = Colors.Black,
                    unfocusedContainerColor = Colors.White,
                    focusedContainerColor = Colors.White,
                ),
                modifier = Modifier.weight(1f),
                maxLines = 1,
                singleLine = true,
            )
            IconButton(onClick = {
                onClickSend()
            }) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Colors.Black)
            }
        }
    }
}