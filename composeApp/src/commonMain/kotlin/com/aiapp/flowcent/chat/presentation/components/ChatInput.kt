package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.graphics.Color
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
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
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()


    val infiniteTransition = rememberInfiniteTransition(label = "glow")

    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glowAnim"
    )

    val glowingBrush = remember(animatedOffset, isFocused) {
        if (isFocused) {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFF2196F3).copy(alpha = 0.2f),
                    Color(0xFF00C853).copy(alpha = 0.2f)
                ),
                start = Offset(animatedOffset, 0f),
                end = Offset(animatedOffset + 300f, 300f)
            )
        } else {
            SolidColor(Color.White.copy(alpha = 0.1f))
        }
    }


    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 4.dp,
                    brush = glowingBrush,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(2.dp) // small padding so the field doesn’t touch border
        ) {
            OutlinedTextField(
                value = state.userText,
                onValueChange = { newValue -> onUpdateText(newValue) },
                modifier = Modifier.fillMaxWidth(),
                interactionSource = interactionSource,
                singleLine = true,
                shape = RoundedCornerShape(32.dp),
                placeholder = { Text("Type your message here") },
                trailingIcon = {
                    IconButton(onClick = onClickSend) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_send),
                            modifier = Modifier.size(20.dp),
                            contentDescription = "Send"
                        )
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground
                ),
                textStyle = MaterialTheme.typography.bodyMedium
            )
        }


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