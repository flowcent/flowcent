package com.aiapp.flowcent.core.presentation.animation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun Modifier.fadeInAndScale(): Modifier {
    var visible by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(500)
    )
    val scaleAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0.95f,
        animationSpec = tween(500)
    )

    LaunchedEffect(Unit) {
        visible = true
    }

    return this.graphicsLayer {
        alpha = alphaAnim
        scaleX = scaleAnim
        scaleY = scaleAnim
    }
}