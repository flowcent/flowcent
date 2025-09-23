package com.aiapp.flowcent.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun AppOutlineButton(
    text: String,
    onClick: () -> Unit,
    gradientColors: List<Color> = listOf(Color(0xFF5769F2), Color(0xFF9036E9)),
    modifier: Modifier = Modifier,
    cornerRadius: Int = 16,
    contentPadding: Int = 12,
    icon: ImageVector? = null,
    iconColor: Color? = null,
    textColor: Color? = null,
    hasGradient: Boolean = true,
    outlineColor: Color = MaterialTheme.colorScheme.primary,
    isLoading: Boolean = false
) {
    val borderBrush = if (hasGradient) {
        Brush.horizontalGradient(colors = gradientColors)
    } else {
        SolidColor(outlineColor)
    }
    val textColorUpdate = textColor ?: if (hasGradient) {
        gradientColors.first()
    } else {
        outlineColor
    }

    Button(
        onClick = onClick,
        enabled = !isLoading, // disable click while loading
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius.dp),
        border = BorderStroke(width = 2.dp, brush = borderBrush),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent
        ),
        contentPadding = PaddingValues(vertical = contentPadding.dp, horizontal = 32.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        color = textColorUpdate,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp)
                    )
                }

                icon != null -> {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor ?: textColorUpdate,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            if (!isLoading) {
                Text(text = text, color = textColorUpdate)
            }
        }
    }
}


