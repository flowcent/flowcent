package com.aiapp.flowcent.userOnboarding.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.aiapp.flowcent.userOnboarding.screens.FeatureItem
import kotlinx.coroutines.delay

@Composable
fun AnimatedFeatureCard(item: FeatureItem, index: Int) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(index * 400L) // staggered delay (120ms between items)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(400)) +
                slideInVertically(initialOffsetY = { it / 2 }, animationSpec = tween(400))
    ) {
        FeatureCard(item)
    }
}
