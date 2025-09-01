package com.aiapp.flowcent.core.presentation.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color


internal val LightColorScheme = lightColorScheme(
//    primary = Colors.LightPrimary,
//    onPrimary = Colors.LightOnPrimary,
//    background = Colors.LightBackground,
//    onBackground = Colors.LightOnBackground,
//    surface = Colors.LightSurface,
//    onSurface = Colors.LightPrimary,
//    secondary = Colors.LightSecondary,
//    onSecondary = Colors.LightOnBackground

    primary = Colors.Blue500,              // Skip text, toggle ON
    onPrimary = Color.White,        // Icon inside blue circle
    primaryContainer = Colors.Blue100,     // Circle background, selected icon
    onPrimaryContainer = Colors.Blue900,   // Icon inside selected container

    background = Color(0xFFF2F2F7),       // Screen background
    surface = Color.White,          // Card backgrounds (if used)
    surfaceVariant = Color.White,       // Input fields, unselected icons
    onSurface = Colors.Black900,        // Main text
    onSurfaceVariant = Colors.Gray600,     // Secondary/supporting text
    outlineVariant = Colors.LightOutline,
    tertiaryContainer = Colors.Gray600
)

internal val DarkColorScheme = darkColorScheme(
//    primary = Colors.DarkPrimary,
//    onPrimary = Colors.DarkOnPrimary,
//    background = Colors.DarkBackground,
//    onBackground = Colors.DarkOnBackground,
//    surface = Colors.DarkSurface,
//    onSurface = Colors.DarkOnBackground,
//    secondary = Colors.DarkSecondary,
//    onSecondary = Colors.DarkOnBackground

    primary = Color(0xFF2979FF),          // Blue accents (Skip, toggle ON)
    onPrimary = Color.White,              // Icon/text on blue
    primaryContainer = Colors.Blue100,     // Circle background, selected icon
    onPrimaryContainer = Colors.Blue900,

    background = Color(0xFF121212),       // Screen background
    surface = Color(0xFF1E1E1E),          // Cards, sections
    surfaceVariant = Color(0xFF1C1C1E),   // Input fields, unselected icons

    onSurface = Color.White,              // Main text
    onSurfaceVariant = Color(0xFFB0B0B0), // Secondary text

    outlineVariant = Color(0xFF2C2C2E),
    tertiaryContainer = Colors.Gray600
)