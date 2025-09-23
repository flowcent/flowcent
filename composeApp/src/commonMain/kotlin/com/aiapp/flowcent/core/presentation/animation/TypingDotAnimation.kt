package com.aiapp.flowcent.core.presentation.animation

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// Define constants for the animation
private const val AnimationDurationMillis = 1200
private const val AnimationDelayMillis = 200
private const val NumDots = 3
private const val MinAlpha = 0.2f
private const val MaxAlpha = 1.0f

@Composable
fun TypingDotAnimation(
    modifier: Modifier = Modifier,
    dotColor: Color = MaterialTheme.colorScheme.primary,
    dotSize: Dp = 10.dp,
    dotSpacing: Dp = 6.dp
) {
    // Create an infinite transition to drive the animation
    val infiniteTransition = rememberInfiniteTransition(label = "typing_indicator_transition")

    // Create a list of animated alpha values for each dot
    val alphaValues = List(NumDots) { index ->
        infiniteTransition.animateAlpha(delay = index * AnimationDelayMillis)
    }

    // Arrange the dots in a Row
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(dotSpacing)
    ) {
        alphaValues.forEach { alpha ->
            Dot(
                alpha = alpha.value,
                color = dotColor,
                size = dotSize
            )
        }
    }
}


@Composable
private fun Dot(alpha: Float, color: Color, size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .background(
                color = color.copy(alpha = alpha),
                shape = CircleShape
            )
    )
}

@Composable
private fun InfiniteTransition.animateAlpha(delay: Int): State<Float> {
    return animateFloat(
        initialValue = MinAlpha,
        targetValue = MinAlpha, // Initial and target values don't matter here due to keyframes
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = AnimationDurationMillis
                // Animate from min to max alpha
                MinAlpha at delay with FastOutSlowInEasing
                MaxAlpha at (delay + AnimationDurationMillis / (NumDots + 1)) with FastOutSlowInEasing
                // Animate back to min alpha
                MinAlpha at (delay + (AnimationDurationMillis / NumDots) * 2) with FastOutLinearInEasing
                MinAlpha at AnimationDurationMillis // Keep it at min for the rest of the cycle
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "dot_alpha_animation"
    )
}
