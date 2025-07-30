package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.presentation.ChatState
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.ic_send
import flowcent.composeapp.generated.resources.ic_voice
import org.jetbrains.compose.resources.painterResource

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
            .height(70.dp)
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = state.userText,
            onValueChange = { newValue ->
                onUpdateText(newValue)
            },
            trailingIcon = {
                IconButton(onClick = {
                    onClickSend()
                }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_send),
                        modifier = Modifier.padding(horizontal = 4.dp).size(20.dp),
                        contentDescription = "Send"
                    )
                }
            },
            shape = RoundedCornerShape(32.dp),
            placeholder = { Text("Type your messenger here") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                cursorColor = MaterialTheme.colorScheme.onBackground,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
            ),
            modifier = Modifier.weight(1f),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.width(16.dp))

        IconButton(onClick = { onClickMic() }) {
            if (isListening) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Close",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            } else {
                Icon(
                    painter = painterResource(Res.drawable.ic_voice),
                    modifier = Modifier.size(40.dp),
                    contentDescription = "Mic",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}