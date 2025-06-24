package com.aiapp.flowcent.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    darkTheme: Boolean = false, // Checks if your system is in dark theme mode.
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography(),
        shapes = Shapes,
        content = content,
    )
}