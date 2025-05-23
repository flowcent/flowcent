/*
 * Created by Saeedus Salehin on 15/5/25, 2:51 PM.
 */

package com.aiapp.flowcent.transaction.presentation.navigation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import com.aiapp.flowcent.core.utils.TimeUtils.getCurrentDate
import com.aiapp.flowcent.transaction.presentation.navigation.components.CalendarStrip

@Composable
fun TransactionHomeScreen() {
    Column {
        CalendarStrip(
            selectedDate = getCurrentDate(),
            onDateSelected = {}
        )
    }
}