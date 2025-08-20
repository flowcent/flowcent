package com.aiapp.flowcent.chat.presentation.screen

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.UserAction
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.ic_send
import flowcent.composeapp.generated.resources.ic_voice
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

    val glowColors = listOf(Color(0xFF00E6F6), Color(0xFF0F1B40))

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

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        // Top Back Button
        Icon(
            imageVector = Icons.Default.ArrowBack, // replace with your resource
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.TopStart)
                .clickable { onBack() }
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Speaking to Echo",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Go ahead, I am listening...",
                color = Color.LightGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Animated glowing circle
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .scale(scale)
                    .background(
                        brush = Brush.radialGradient(glowColors),
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                text = chatState.originalVoiceText.ifEmpty {
                    "Can you recommend any Top Resources for learning UI"
                },
                color = Color.Black,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                maxLines = 8
            )

        }

        // Bottom controls
        Row(
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_send), // replace
                contentDescription = "Send",
                tint = Color.Black,
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onSend() }
            )

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(0xFF00E6F6),
                                Color(0xFF001018)
                            )
                        )
                    )
                    .clickable {
                        onClickMic()
                    }
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Mic",
                    tint = Color.White,
                    modifier = Modifier.align(Alignment.Center).size(40.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.Close, // replace
                contentDescription = "Close",
                tint = Color.Black,
                modifier = Modifier
                    .size(36.dp)
                    .clickable { onClose() }
            )
        }
    }
}
