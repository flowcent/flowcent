package com.aiapp.flowcent.core.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AnimatedRoundedProgressBar(
    targetProgress: Float,
    progressColor: Color,
    trackColor: Color = Color.LightGray.copy(alpha = 0.3f),
    delayMillis: Long = 300,
    durationMillis: Int = 1000,
    modifier: Modifier = Modifier
) {

    val progressAnim = remember { Animatable(0f) }

    LaunchedEffect(targetProgress) {
        delay(delayMillis)
        progressAnim.animateTo(
            targetValue = targetProgress.coerceIn(0f, 1f),
            animationSpec = tween(durationMillis = durationMillis)
        )
    }

    LinearProgressIndicator(
        progress = { progressAnim.value },
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp),
        color = progressColor,
        trackColor = trackColor,
        strokeCap = StrokeCap.Round,
        drawStopIndicator = {}
    )
}
