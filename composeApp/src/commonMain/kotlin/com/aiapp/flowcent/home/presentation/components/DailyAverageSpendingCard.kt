/*
 * Created by Saeedus Salehin on 4/8/25, 10:50 PM.
 */

package com.aiapp.flowcent.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.core.presentation.animation.fadeInAndScale
import com.aiapp.flowcent.core.presentation.utils.asCurrency

@Composable
fun DailyAverageSpendingCard(
    dailyAverage: Float,
    previousMonthAverage: Float? = null,
    modifier: Modifier = Modifier
) {
    val textColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val subTextColor = if (isSystemInDarkTheme()) Color.Gray else Color.DarkGray

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

    Column(
        modifier = modifier
            .fillMaxWidth(0.5f)
            .fadeInAndScale()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Text(
            text = "Daily Avg Spend",
            color = subTextColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = dailyAverage.asCurrency(),
            color = textColor,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        if (trendText.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = trendText,
                color = trendColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}