/*
 * Created by Saeedus Salehin on 15/5/25, 2:51 PM.
 */

package com.aiapp.flowcent.home.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aiapp.flowcent.core.presentation.components.SpendingCard
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentDate
import com.aiapp.flowcent.home.presentation.HomeState
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.home.presentation.UiEvent
import com.aiapp.flowcent.home.presentation.UserAction
import com.aiapp.flowcent.home.presentation.components.CalendarStrip
import com.aiapp.flowcent.home.presentation.navigation.HomeNavRoutes
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    homeState: HomeState,
    navController: NavController
) {

    LaunchedEffect(key1 = homeState.selectedDate) {
        homeViewModel.onAction(UserAction.FetchLatestTransactions)
    }

    LaunchedEffect(key1 = Unit) {
        homeViewModel.onAction(UserAction.FetchTotalAmount)
    }

    LaunchedEffect(key1 = rememberScaffoldState()) {
        homeViewModel.uiEvent.collect {
            when (it) {
                UiEvent.NavigateToAuth -> navController.navigate(HomeNavRoutes.AuthScreen.route)
                is UiEvent.ShowSnackbar -> {}
            }
        }
    }


    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { homeViewModel.onAction(UserAction.NavigateToAuth) }) {
                Icon(
                    painter = painterResource(Res.drawable.compose_multiplatform),
                    contentDescription = "Profile",
                    modifier = Modifier.size(24.dp)
                )
            }
            Row {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(Res.drawable.compose_multiplatform), // Placeholder
                        contentDescription = "Insight",
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(Res.drawable.compose_multiplatform), // Placeholder
                        contentDescription = "Notifications",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

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
