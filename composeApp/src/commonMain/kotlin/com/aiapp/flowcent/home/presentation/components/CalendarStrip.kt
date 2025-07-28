/*
 * Created by Saeedus Salehin on 23/5/25, 10:40 PM.
 */

package com.aiapp.flowcent.home.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.daysInMonth
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.ic_arrow_left
import flowcent.composeapp.generated.resources.ic_arrow_right
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
    var selectedDateState by remember { mutableStateOf(selectedDate) }

    var currentMonthStart by remember {
        mutableStateOf(
            LocalDate(
                selectedDateState.year,
                selectedDateState.month,
                1
            )
        )
    }

    val lazyListState = rememberLazyListState()


    Column(modifier = modifier.padding(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = {
                    currentMonthStart = currentMonthStart.minus(1, DateTimeUnit.MONTH)
                }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_left),
                    contentDescription = "Previous Month",
                    tint = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.size(24.dp)
                )
            }

            AnimatedContent(
                targetState = currentMonthStart,
                transitionSpec = { fadeIn() togetherWith fadeOut() }
            ) { targetDate ->
                Text(
                    text = "${
                        targetDate.month.name.lowercase().replaceFirstChar { it.uppercase() }
                    } ${targetDate.year}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.inverseSurface,
                )
            }

            IconButton(
                onClick = {
                    currentMonthStart = currentMonthStart.plus(1, DateTimeUnit.MONTH)
                }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_right),
                    contentDescription = "Next Month",
                    tint = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedContent(
            targetState = currentMonthStart,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    slideInHorizontally { width -> -width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> width } + fadeOut()
                }
            }
        ) { targetDate ->
            val days = daysInMonth(targetDate)
            val dates = (1..days).map {
                LocalDate(targetDate.year, targetDate.month, it)
            }
            val density = LocalDensity.current

            LaunchedEffect(targetDate, selectedDateState) {
                if (selectedDateState.month == selectedDateState.month && selectedDateState.year == selectedDateState.year) {
                    val index = selectedDateState.dayOfMonth - 1
                    val offset = with(density) { 140.dp.toPx() }.toInt()
                    if (index >= 0 && index < dates.size) {
                        lazyListState.animateScrollToItem(index, -offset)
                    }
                }
            }
            val isDarkTheme = isSystemInDarkTheme()

            LazyRow(
                state = lazyListState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(dates) { date ->
                    val isSelected = date == selectedDateState

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onDateSelected(date)
                                selectedDateState = date
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 12.dp, horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val textColor = if (isSelected) {
                                if (isDarkTheme) Color.White else Color.Black
                            } else {
                                MaterialTheme.colorScheme.inverseSurface.copy(alpha = 0.6f)
                            }

                            // day-of-week initial
                            Text(
                                text = date.dayOfWeek.name.first().toString(),
                                style = MaterialTheme.typography.labelSmall,
                                color = textColor
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // day-of-month
                            Text(
                                text = date.dayOfMonth.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = textColor
                            )
                        }
                    }
                }
            }
        }
    }
}