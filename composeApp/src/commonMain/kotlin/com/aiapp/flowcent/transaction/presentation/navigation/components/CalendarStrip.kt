/*
 * Created by Saeedus Salehin on 23/5/25, 10:40 PM.
 */

package com.aiapp.flowcent.transaction.presentation.navigation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.ui.theme.green
import com.aiapp.flowcent.core.ui.theme.medium14Black
import com.aiapp.flowcent.core.utils.DateTimeUtils
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.compose_multiplatform
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import org.jetbrains.compose.resources.painterResource

@Composable
fun CalendarStrip(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentMonthStart by remember { mutableStateOf(LocalDate(selectedDate.year, selectedDate.month, 1)) }
    val daysInMonth = DateTimeUtils.daysInMonth(currentMonthStart)
    val dates = remember(currentMonthStart) {
        (1..daysInMonth).map {
            LocalDate(currentMonthStart.year, currentMonthStart.month, it)
        }
    }

    Column(modifier = modifier.padding(8.dp)) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                currentMonthStart = currentMonthStart.minus(1, DateTimeUnit.MONTH)
            }) {
                Icon(
                    painter = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = "Previous Month"
                )
            }

            Text(
                text = "${currentMonthStart.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${currentMonthStart.year}",
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(onClick = {
                currentMonthStart = currentMonthStart.plus(1, DateTimeUnit.MONTH)
            }) {
                Icon(
                    painter = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = "Next Month"
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(dates) { date ->
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
}