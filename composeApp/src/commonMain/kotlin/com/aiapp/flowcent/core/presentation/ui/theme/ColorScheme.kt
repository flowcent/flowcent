package com.aiapp.flowcent.core.presentation.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme


internal val LightColorScheme = lightColorScheme(
    primary = Colors.LightPrimary,
    onPrimary = Colors.LightOnPrimary,
    background = Colors.LightBackground,
    onBackground = Colors.LightOnBackground,
    surface = Colors.LightSurface,
    onSurface = Colors.LightPrimary,
    secondary = Colors.LightSecondary,
    onSecondary = Colors.LightOnBackground
)

internal val DarkColorScheme = darkColorScheme(
    primary = Colors.DarkPrimary,
    onPrimary = Colors.DarkOnPrimary,
    background = Colors.DarkBackground,
    onBackground = Colors.DarkOnBackground,
    surface = Colors.DarkSurface,
    onSurface = Colors.DarkOnBackground,
    secondary = Colors.DarkSecondary,
    onSecondary = Colors.DarkOnBackground
)