/*
 * Created by Saeedus Salehin on 23/5/25, 10:40 PM.
 */

package com.aiapp.flowcent.transaction.presentation.navigation.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.ui.theme.green
import com.aiapp.flowcent.core.ui.theme.medium14Black
import kotlinx.datetime.*

@Composable
fun CalendarStrip(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    val weekStart = selectedDate.minus(selectedDate.dayOfWeek.ordinal.toLong(), DateTimeUnit.DAY)
    val days = (0..6).map { weekStart.plus(it.toLong(), DateTimeUnit.DAY) }

    Row(
        modifier = Modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        days.forEach { date ->
            val isSelected = date == selectedDate
            Column(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isSelected) green else Color.Transparent)
                    .clickable { onDateSelected(date) }
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = date.dayOfWeek.name.first().toString(),
                    style = medium14Black()
                )
                Text(
                    text = date.dayOfMonth.toString(),
                    style = medium14Black()
                )
            }
        }
    }
}
