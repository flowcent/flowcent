/*
 * Created by Saeedus Salehin on 15/5/25, 2:51 PM.
 */

package com.aiapp.flowcent.home.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentDate
import com.aiapp.flowcent.home.presentation.HomeState
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.home.presentation.components.CalendarStrip

@Composable
fun HomeScreen(homeViewModel: HomeViewModel, homeState: HomeState) {
    Scaffold { padding ->
        Column(modifier = Modifier.padding(padding)) {
            CalendarStrip(
                selectedDate = getCurrentDate(),
                onDateSelected = {}
            )
        }
    }
}