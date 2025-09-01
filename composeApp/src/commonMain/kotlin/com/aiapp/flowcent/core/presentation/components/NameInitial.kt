package com.aiapp.flowcent.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.aakira.napier.Napier
import kotlin.random.Random

@Composable
fun NameInitial(
    text: String,
    textSize: TextUnit = 16.sp,
    bgColor: Color = randomColor(),
    size: Dp = 40.dp,
    modifier: Modifier = Modifier
) {
    Napier.e("Sohan NameInitial text $text")
    val initials = text
        .trim()
        .take(2)
        .uppercase()
        .ifEmpty { "??" }

    Box(
        modifier = Modifier
            .size(size)
            .background(color = bgColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = textSize
            ),
            color = MaterialTheme.colorScheme.background,
        )
    }
}

@Composable
fun randomColor(): Color {
    val colors = listOf(
        Color(0xFFE57373),
        Color(0xFF64B5F6),
        Color(0xFF81C784),
        Color(0xFFFFB74D),
        Color(0xFFBA68C8)
    )
    return colors[Random.nextInt(colors.size)]
}