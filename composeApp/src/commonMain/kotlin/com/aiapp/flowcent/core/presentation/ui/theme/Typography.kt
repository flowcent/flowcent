package com.aiapp.flowcent.core.presentation.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import flowcent.composeapp.generated.resources.OpenSans_Bold
import flowcent.composeapp.generated.resources.OpenSans_ExtraBold
import flowcent.composeapp.generated.resources.OpenSans_Light
import flowcent.composeapp.generated.resources.OpenSans_Medium
import flowcent.composeapp.generated.resources.OpenSans_Regular
import flowcent.composeapp.generated.resources.OpenSans_SemiBold
import flowcent.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font


@Composable
fun appTypography(): Typography {
    val AppFontFamily = FontFamily(
        Font(Res.font.OpenSans_Bold, FontWeight.Bold),
        Font(Res.font.OpenSans_Regular, FontWeight.Normal),
        Font(Res.font.OpenSans_ExtraBold, FontWeight.ExtraBold),
        Font(Res.font.OpenSans_Medium, FontWeight.Medium),
        Font(Res.font.OpenSans_SemiBold, FontWeight.SemiBold),
        Font(Res.font.OpenSans_Light, FontWeight.Light),
    )

    return Typography(
        displayLarge = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 58.sp
        ),
        displayMedium = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 44.sp
        ),
        displaySmall = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 36.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 28.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp
        ),
        titleLarge = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp
        ),
        titleMedium = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        ),
        titleSmall = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        ),
        bodySmall = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Light,
            fontSize = 12.sp
        ),
        labelLarge = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp
        ),
        labelMedium = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp
        ),
        labelSmall = TextStyle(
            fontFamily = AppFontFamily,
            fontWeight = FontWeight.Light,
            fontSize = 11.sp
        ),
    )
}

