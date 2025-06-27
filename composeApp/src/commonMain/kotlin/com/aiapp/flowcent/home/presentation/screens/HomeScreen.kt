/*
 * Created by Saeedus Salehin on 15/5/25, 2:51 PM.
 */

package com.aiapp.flowcent.home.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.presentation.components.SpendingCard
import com.aiapp.flowcent.core.presentation.ui.theme.RadialGradientBackground
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentDate
import com.aiapp.flowcent.home.presentation.HomeState
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.home.presentation.UserAction
import com.aiapp.flowcent.home.presentation.components.CalendarStrip

@Composable
fun HomeScreen(homeViewModel: HomeViewModel, homeState: HomeState) {

    LaunchedEffect(Unit) {
        homeViewModel.onAction(UserAction.FetchLatestTransactions)
    }

    Scaffold { padding ->
        RadialGradientBackground {
            Column(modifier = Modifier.padding(padding)) {
                CalendarStrip(
                    selectedDate = getCurrentDate(),
                    onDateSelected = {}
                )

                Text(
                    text = "Latest Transaction",
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(homeState.latestTransactions.size) { index ->
                        val transaction = homeState.latestTransactions[index]
                        SpendingCard(expenseItem = transaction)
                    }
                }
            }
        }
    }
}