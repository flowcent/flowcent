/*
 * Created by Saeedus Salehin on 15/5/25, 2:51 PM.
 */

package com.aiapp.flowcent.home.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.aiapp.flowcent.core.utils.DateTimeUtils.getCurrentDate
import com.aiapp.flowcent.home.presentation.components.CalendarStrip

@Composable
fun HomeScreen() {
    Column {
        CalendarStrip(
            selectedDate = getCurrentDate(),
            onDateSelected = {}
        )
    }
}