/*
 * Created by Saeedus Salehin on 15/5/25, 2:51 PM.
 */

package com.aiapp.flowcent.home.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.presentation.components.SpendingCard
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentDate
import com.aiapp.flowcent.home.presentation.HomeState
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.home.presentation.UserAction
import com.aiapp.flowcent.home.presentation.components.CalendarStrip

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    homeState: HomeState
) {

    LaunchedEffect(key1 = homeState.selectedDate) {
        homeViewModel.onAction(UserAction.FetchLatestTransactions)
    }

    LaunchedEffect(key1 = Unit) {
        homeViewModel.onAction(UserAction.FetchTotalAmount)
    }


    Column {
        CalendarStrip(
            selectedDate = getCurrentDate(),
            onDateSelected = {
                println("Sohan selected date: $it")
                homeViewModel.onAction(UserAction.SetSelectedDate(it))
            }
        )

        Column {
            Text(
                text = "Latest Transaction",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(homeState.latestTransactions.size) { index ->
                    val transaction = homeState.latestTransactions[index]
                    SpendingCard(expenseItem = transaction)
                }
            }
        }
    }
}