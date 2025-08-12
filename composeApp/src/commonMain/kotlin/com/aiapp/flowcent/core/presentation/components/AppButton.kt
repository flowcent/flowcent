package com.aiapp.flowcent.core.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    shape: Shape = RoundedCornerShape(8.dp),
    height: Dp = 60.dp,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    clickCooldownMillis: Long = 1000, // 1 second default
    leadingIcon: (@Composable (() -> Unit))? = null
) {
    var lastClickTime by remember { mutableStateOf<Instant?>(null) }

    val disabledBgColor = backgroundColor.copy(alpha = 0.5f)

    fun safeClick() {
        val now = Clock.System.now()
        if (lastClickTime == null || now.toEpochMilliseconds() - lastClickTime!!.toEpochMilliseconds() >= clickCooldownMillis) {
            lastClickTime = now
            onClick()
        }
    }

    Button(
        onClick = { safeClick() },
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = shape,
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            disabledBackgroundColor = disabledBgColor
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = textColor,
                strokeWidth = 2.dp
            )
        } else {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = textStyle,
                color = textColor
            )
        }
    }
}

