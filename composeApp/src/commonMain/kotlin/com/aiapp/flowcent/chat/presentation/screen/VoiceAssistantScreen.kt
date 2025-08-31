package com.aiapp.flowcent.chat.presentation.screen

import CircularIcon
import CircularIconButton
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.UserAction
import com.aiapp.flowcent.chat.presentation.components.MicButton
import com.aiapp.flowcent.core.presentation.animation.VoiceWaveAnimation
import com.aiapp.flowcent.core.presentation.ui.theme.Colors
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.lucide_send
import org.jetbrains.compose.resources.painterResource

@Composable
fun VoiceAssistantScreen(
    modifier: Modifier,
    chatState: ChatState,
    viewModel: ChatViewModel,
) {
    val infiniteTransition = rememberInfiniteTransition()

    // Scale animation for glowing effect
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        )
    )

    val glowColors = listOf(
        Color(0xFF0F1B40),
        Color(0xFF001018),
        Colors.GradientOne,
        Colors.GradientTwo,
    )

    fun onClickMic() {
        if (chatState.isListening) {
            viewModel.onAction(UserAction.StopAudioPlayer)
        } else {
            viewModel.onAction(UserAction.StartAudioPlayer)
        }
    }

    fun onBack() {
        viewModel.onAction(UserAction.NavigateToBack)
    }

    fun onSend() {
        viewModel.onAction(UserAction.SendVoiceToChat(chatState.originalVoiceText))
    }

    fun onClose() {
        viewModel.onAction(UserAction.StopAudioPlayer)
        viewModel.onAction(UserAction.UpdateVoiceText("", ""))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Speaking to Marcus",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Go ahead, I am listening...",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Animated glowing circle
            VoiceWaveAnimation()

            Spacer(modifier = Modifier.height(60.dp))

            if (chatState.originalVoiceText.isNotEmpty()) {
                Text(
                    text = chatState.originalVoiceText,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    maxLines = 8
                )
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "I am listening... Tell me something like:",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "- I have bought a shirt for $50",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "- Just Paid $1200 rent",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "- Got my $5500 salary for this month",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "And I will do the rest",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }


        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                CircularIconButton(
                    icon = CircularIcon.Vector(Icons.Default.Sell),
                    iconSize = 56.dp,
                    iconColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surface,
                    onClick = { onSend() }
                )

                MicButton(
                    size = 70.dp,
                    onClickMic = { onClickMic() },
                )

                CircularIconButton(
                    icon = CircularIcon.Vector(Icons.Default.Close),
                    iconSize = 56.dp,
                    iconColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surface,
                    onClick = { onClose() }
                )
            }
        }

    }
}
