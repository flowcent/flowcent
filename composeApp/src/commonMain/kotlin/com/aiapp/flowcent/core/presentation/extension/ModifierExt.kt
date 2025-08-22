package com.aiapp.flowcent.core.presentation.extension

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

fun Modifier.dottedBorder(
    color: Color,
    strokeWidth: Dp,
    cornerRadius: Dp,
    dotInterval: Dp
): Modifier = this.then(
    Modifier.drawBehind {
        val stroke = Stroke(
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(dotInterval.toPx(), dotInterval.toPx()), 0f
            )
        )
        val cornerRadiusPx = cornerRadius.toPx()
        drawRoundRect(
            color = color,
            size = size,
            cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
            style = stroke
        )
    }
)
