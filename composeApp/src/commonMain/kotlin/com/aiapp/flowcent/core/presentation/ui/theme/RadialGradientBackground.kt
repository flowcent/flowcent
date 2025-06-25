package com.aiapp.flowcent.core.presentation.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun RadialGradientBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFC3E1E3),
                        Color(0xFFD6EDD3), // Center green
                        Color(0xFFC8E6C9), // Transition color
                        Color(0xFFC3E1E3)  // Outer blue-teal
                    ),
                )
            )
    ) {
        content()
    }
}