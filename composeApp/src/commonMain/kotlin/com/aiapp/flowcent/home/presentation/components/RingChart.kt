package com.aiapp.flowcent.home.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.presentation.animation.fadeInAndScale
import com.aiapp.flowcent.core.presentation.utils.asCurrency

@Composable
fun RingChart(
    actual: Float,
    target: Float,
    dailyAverage: Float,
    previousMonthAverage: Float?,
    label: String = "Spend",
    labelStyle: TextStyle = MaterialTheme.typography.bodyLarge.copy(
        color = MaterialTheme.colorScheme.onSurface,
        fontWeight = FontWeight.SemiBold
    ),
    valueColor: Color = Color(0xFFFF375F),
    valueColorBackground: Color = Color(0XFFFF8A8A),
    ringSize: Dp = 100.dp,
    horizontalGap: Dp = 32.dp,
    modifier: Modifier = Modifier,
) {
    val progress = remember { Animatable(0f) }
    val isSystemInDarkTheme = isSystemInDarkTheme()

    LaunchedEffect(actual, target) {
        progress.animateTo(
            targetValue = if (target > 0) actual / target else 0f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    val animatedSpent = (progress.value * target).toInt()

    val subTextColor = if (isSystemInDarkTheme) Color.Gray else Color.DarkGray
    val diff = previousMonthAverage?.let { dailyAverage - it }
    val trendText = when {
        diff == null -> ""
        diff > 0 -> "▲ +${diff.asCurrency()} from last month"
        diff < 0 -> "▼ -${(-diff).asCurrency()} from last month"
        else -> "No change from last month"
    }
    val trendColor = when {
        diff == null -> subTextColor
        diff > 0 -> Color.Red.copy(alpha = 0.8f)
        diff < 0 -> Color(0xFF4CAF50)
        else -> subTextColor
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .fadeInAndScale()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = horizontalGap, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(ringSize),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = size.width / 4f

                // Background Ring
                drawArc(
                    color = valueColorBackground,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )

                // Progress Ring
                drawArc(
                    brush = Brush.linearGradient(
                        listOf(valueColor, valueColor)
                    ),
                    startAngle = -90f,
                    sweepAngle = 360 * progress.value,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        Spacer(modifier = Modifier.width(horizontalGap))

        // Text Info
        Column {
            Text(
                text = label,
                style = labelStyle,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$animatedSpent/${target.toInt()}",
                color = valueColor,
                style = labelStyle,
            )

//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = "Daily Avg Spend",
//                color = subTextColor,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Medium
//            )
//
//            Text(
//                text = dailyAverage.asCurrency(),
//                color = MaterialTheme.colorScheme.inverseSurface,
//                fontSize = 16.sp,
//                fontWeight = FontWeight.Bold
//            )
//
//            if (trendText.isNotEmpty()) {
//                Spacer(modifier = Modifier.height(4.dp))
//                Text(
//                    text = trendText,
//                    color = trendColor,
//                    fontSize = 12.sp,
//                    fontWeight = FontWeight.Normal
//                )
//            }
        }
    }
}



