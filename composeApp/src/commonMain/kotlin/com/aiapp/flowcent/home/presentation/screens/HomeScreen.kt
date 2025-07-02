/*
 * Created by Saeedus Salehin on 15/5/25, 2:51 PM.
 */

package com.aiapp.flowcent.home.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.presentation.components.SpendingCard
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

    LaunchedEffect(Unit) {
        homeViewModel.onAction(UserAction.FetchLatestTransactions)
    }


    Column {
        CalendarStrip(
            selectedDate = getCurrentDate(),
            onDateSelected = {}
        )

        Column {
            Text(
                text = "Latest Transaction",
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