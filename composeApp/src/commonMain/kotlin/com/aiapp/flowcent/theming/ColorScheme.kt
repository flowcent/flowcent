package com.aiapp.flowcent.theming

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme


internal val LightColorScheme = lightColorScheme(
    primary = Colors.LightPrimary,
    background = Colors.LightBackground,
    onBackground = Colors.LightOnBackground,
    surface = Colors.LightSurface,
    onSurface = Colors.LightPrimary,
    secondary = Colors.LightSecondary,
    onSecondary = Colors.LightOnBackground,
)

internal val DarkColorScheme = darkColorScheme(
    primary = Colors.DarkPrimary,
    background = Colors.DarkBackground,
    onBackground = Colors.DarkOnBackground,
    surface = Colors.DarkSurface,
    onSurface = Colors.DarkPrimary,
    secondary = Colors.DarkSecondary,
    onSecondary = Colors.DarkOnBackground
)