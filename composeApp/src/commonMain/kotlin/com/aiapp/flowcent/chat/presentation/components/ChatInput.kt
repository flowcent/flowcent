package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.presentation.ChatState

@Composable
fun ChatInput(
    state: ChatState,
    isListening: Boolean = false,
    onUpdateText: (text: String) -> Unit = {},
    onClickMic: () -> Unit = {},
    onClickSend: () -> Unit = {},
) {

    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .background(Color(0xFF000000))
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onClickMic() }) {
            if(isListening){
                Icon(Icons.Default.Close, contentDescription = "Close", tint = Color(0xFFFFFFFF))
            }else{
                Icon(Icons.Default.Mic, contentDescription = "Mic", tint = Color(0xFFFFFFFF))
            }
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