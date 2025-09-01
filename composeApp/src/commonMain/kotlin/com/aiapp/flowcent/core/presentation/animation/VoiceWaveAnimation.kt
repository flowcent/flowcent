package com.aiapp.flowcent.core.presentation.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun VoiceWaveAnimation(
    circleColor: Color = Color.Magenta,
    circleCount: Int = 6,
    animationDelayMillis: Int = 2000,
    modifier: Modifier = Modifier.size(200.dp)
) {
    val transition = rememberInfiniteTransition()

    // Create one animation per circle with staggered start times
    val delays = List(circleCount) { index ->
        index * (animationDelayMillis / circleCount)
    }

    val scales = delays.map { delay ->
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = animationDelayMillis, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
                initialStartOffset = StartOffset(delay)
            )
        )
    }

    Canvas(modifier = modifier) {
        val center = size.center
        val maxRadius = size.minDimension / 2f

        scales.forEach { anim ->
            val fraction = anim.value
            drawCircle(
                color = circleColor.copy(alpha = 1 - fraction),
                radius = maxRadius * fraction,
                center = center,
                style = Stroke(width = 4.dp.toPx())
            )
        }
    }
}
