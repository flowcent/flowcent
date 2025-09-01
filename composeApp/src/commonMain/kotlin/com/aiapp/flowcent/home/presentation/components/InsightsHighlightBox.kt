package com.aiapp.flowcent.home.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.core.presentation.animation.fadeInAndScale

@Composable
fun InsightsHighlightBox(
    modifier: Modifier = Modifier,
    onClicked: () -> Unit
) {
    Card(
        modifier = modifier
            .height(150.dp)
            .fadeInAndScale()
            .clickable { onClicked() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Insights",
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.inverseSurface
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Insights",
                    tint = MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
            }

            HorizontalDivider()

            // TODO remove placeholder with actual graph
            Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                val points = listOf(
                    Offset(0f, size.height * 0.5f),
                    Offset(size.width * 0.2f, size.height * 0.3f),
                    Offset(size.width * 0.4f, size.height * 0.6f),
                    Offset(size.width * 0.6f, size.height * 0.4f),
                    Offset(size.width * 0.8f, size.height * 0.7f),
                    Offset(size.width, size.height * 0.6f)
                )
                val path = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        lineTo(points[i].x, points[i].y)
                    }
                }
                drawPath(
                    path = path,
                    color = Color(0xFF4CAF50),
                    style = Stroke(width = 4.dp.toPx())
                )
            }
        }
    }
}