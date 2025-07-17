package com.aiapp.flowcent.core.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Checks if your system is in dark theme mode.
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colors,
        typography = appTypography(),
        shapes = Shapes,
    ){
        RadialGradientBackground(darkTheme = darkTheme) {
            content()
        }
    }
}