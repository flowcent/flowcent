package com.aiapp.flowcent.core.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LineGraph(modifier: Modifier = Modifier) {
    val isDarkTheme = isSystemInDarkTheme()
    val dummyData = remember {
        mapOf(
            "Food" to 120f,
            "Transport" to 80f,
            "Entertainment" to 150f,
            "Bills" to 90f,
            "Other" to 50f
        )
    }
    val categories = dummyData.keys.toList()
    val amounts = dummyData.values.toList()
    val maxAmount = amounts.maxOrNull() ?: 0f

    val animatable = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        animatable.animateTo(1f, animationSpec = tween(durationMillis = 1500))
    }

    val textMeasurer = rememberTextMeasurer()

    Column(modifier = modifier.padding(16.dp)) {
        Canvas(modifier = Modifier.fillMaxWidth().height(250.dp)) { // Increased height for labels
            val graphHeight = size.height - 50.dp.toPx() // Allocate space for labels at the bottom
            val path = Path()
            val stepX = size.width / (categories.size - 1)
            val stepY = graphHeight / maxAmount

            path.moveTo(0f, graphHeight - (amounts[0] * stepY * animatable.value))

            for (i in 0 until categories.size - 1) {
                val x1 = i * stepX
                val y1 = graphHeight - (amounts[i] * stepY * animatable.value)
                val x2 = (i + 1) * stepX
                val y2 = graphHeight - (amounts[i + 1] * stepY * animatable.value)
                val controlX1 = (x1 + x2) / 2f
                val controlY1 = y1
                val controlX2 = (x1 + x2) / 2f
                val controlY2 = y2
                path.cubicTo(controlX1, controlY1, controlX2, controlY2, x2, y2)
            }

            drawPath(
                path = path,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2196F3),
                        Color(0xFF00C853),
                        Color.Transparent
                    )
                ),
                style = Stroke(width = 3.dp.toPx())
            )

            val fillPath = Path().apply {
                addPath(path)
                lineTo(size.width, graphHeight) // Fill down to the graphHeight line
                lineTo(0f, graphHeight)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF87EBDF).copy(alpha = 0.3f),
                        Color(0xFF87EBDF).copy(alpha = 0.1f),
                        Color.Transparent
                    )
                )
            )

            // Draw labels
            categories.forEachIndexed { i, category ->
                val x = i * stepX
                val textStyle = TextStyle(
                    color = if (isDarkTheme) Color.White else Color.Black,
                    fontSize = 10.sp
                ) // Use sp for font size
                val textLayoutResult = textMeasurer.measure(
                    text = category,
                    style = textStyle
                )
                val textWidth = textLayoutResult.size.width.toFloat()
                val textHeight = textLayoutResult.size.height.toFloat()

                // Position text below the graph, centered on the x-axis point
                drawText(
                    textMeasurer = textMeasurer,
                    text = category,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        x - textWidth / 2,
                        size.height - textHeight - 5.dp.toPx()
                    ),
                    style = textStyle
                )
            }
        }
    }
}