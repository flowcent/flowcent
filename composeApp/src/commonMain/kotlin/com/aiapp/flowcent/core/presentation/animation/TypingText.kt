package com.aiapp.flowcent.core.presentation.animation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.delay

@Composable
fun TypingText(
    fullText: String,
    modifier: Modifier = Modifier,
    typingSpeed: Long = 40L, // delay between characters (ms)
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = MaterialTheme.colorScheme.onSurface,
    maxLines: Int = Int.MAX_VALUE,
) {
    var displayedText by remember { mutableStateOf("") }

    LaunchedEffect(fullText) {
        displayedText = ""
        fullText.forEachIndexed { index, _ ->
            displayedText = fullText.take(index + 1)
            delay(typingSpeed)
        }
    }

    Text(
        text = displayedText,
        style = style,
        color = color,
        maxLines = maxLines,
        modifier = modifier
    )
}
