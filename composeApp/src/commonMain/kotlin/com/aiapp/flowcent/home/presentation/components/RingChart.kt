package com.aiapp.flowcent.home.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RingChart(
    spent: Float,
    budget: Float,
    modifier: Modifier = Modifier,
) {
    val progress = remember { Animatable(0f) }
    val isSystemInDarkTheme = isSystemInDarkTheme()

    // Animation state for fade + scale
    var visible by remember { mutableStateOf(false) }
    val alphaAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(500)
    )
    val scaleAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0.95f,
        animationSpec = tween(500)
    )

    LaunchedEffect(Unit) {
        visible = true
    }


    LaunchedEffect(spent, budget) {
        progress.animateTo(
            targetValue = if (budget > 0) spent / budget else 0f,
            animationSpec = tween(durationMillis = 1000)
        )
    }

    val animatedSpent = (progress.value * budget).toInt()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = alphaAnim
                scaleX = scaleAnim
                scaleY = scaleAnim
            }
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 32.dp, vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(100.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = size.width / 4f

                // Background Ring
                drawArc(
                    color = Color(if (isSystemInDarkTheme) 0xFF2C2C2E else 0xFFF2F2F7),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )

                // Progress Ring
                drawArc(
                    brush = Brush.linearGradient(
                        listOf(Color(0xFFFF2D55), Color(0xFFFF375F))
                    ),
                    startAngle = -90f,
                    sweepAngle = 360 * progress.value,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
        }

        Spacer(modifier = Modifier.width(32.dp))

        // Text Info
        Column {
            Text(
                text = "Spent",
                color = MaterialTheme.colorScheme.inverseSurface.copy(0.8f),
                fontSize = 16.sp
            )
            Text(
                text = "$animatedSpent/${budget.toInt()} BDT",
                color = Color(0xFFFF375F),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
        }
    }
}


