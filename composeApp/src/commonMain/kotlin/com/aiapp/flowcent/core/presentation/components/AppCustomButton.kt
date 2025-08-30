package com.aiapp.flowcent.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppCustomButton(
    modifier: Modifier = Modifier,
    btnText: String,
    onClick: () -> Unit,
    isEnabled: Boolean = true,
    iconRes: DrawableResource? = null,
    shouldFillMaxWidth: Boolean = true,
    iconPadding: Dp = 8.dp,
    textColor: Color = Color.Black,
    bgColor: Color = Color.White,
    iconSize: Dp = 28.dp
) {

    val shape = RoundedCornerShape(100.dp)

    Button(
        onClick = onClick,
        enabled = isEnabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = bgColor,
            contentColor = textColor,
            disabledBackgroundColor = Color.Gray,
            disabledContentColor = Color.LightGray
        ),
        modifier = if (shouldFillMaxWidth) modifier.fillMaxWidth() else modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            iconRes?.let {
                val iconPainter = painterResource(it)
                Image(
                    painter = iconPainter,
                    contentDescription = null,
                    modifier = Modifier.size(iconSize).padding(end = iconPadding)
                )
            }
            Text(
                text = btnText,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}
