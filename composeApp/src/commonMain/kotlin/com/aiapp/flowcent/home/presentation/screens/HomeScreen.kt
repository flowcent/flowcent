/*
 * Created by Saeedus Salehin on 15/5/25, 2:51 PM.
 */

package com.aiapp.flowcent.home.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aiapp.flowcent.home.presentation.components.RingChart
import com.aiapp.flowcent.core.presentation.components.SpendingCard
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentDate
import com.aiapp.flowcent.home.presentation.HomeState
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.home.presentation.UiEvent
import com.aiapp.flowcent.home.presentation.UserAction
import com.aiapp.flowcent.home.presentation.components.BalanceHighlightBox
import com.aiapp.flowcent.home.presentation.components.CalendarStrip
import com.aiapp.flowcent.home.presentation.components.DailyAverageSpendingCard
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    homeState: HomeState,
    globalNavController: NavHostController
) {
    val allTransactions = homeState.latestTransactions.flatten()
    val listState = rememberLazyListState()
    val isScrolled by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 } }

    LaunchedEffect(key1 = homeState.selectedDate) {
        homeViewModel.onAction(UserAction.FetchUserUId)
    }

    LaunchedEffect(Unit) {
        homeViewModel.uiEvent.collect {
            when (it) {
                UiEvent.NavigateToAuth -> globalNavController.navigate(AppNavRoutes.Auth.route)
                is UiEvent.ShowSnackbar -> {}
                UiEvent.NavigateToProfile -> globalNavController.navigate(AppNavRoutes.Profile.route)
            }
        }
    }


    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { homeViewModel.onAction(UserAction.NavigateToProfile) }) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile",
                    tint = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
            Row {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(Res.drawable.compose_multiplatform), // Placeholder
                        contentDescription = "Insight",
                        tint = MaterialTheme.colorScheme.inverseSurface,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(Res.drawable.compose_multiplatform), // Placeholder
                        contentDescription = "Notifications",
                        tint = MaterialTheme.colorScheme.inverseSurface,
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

        Column(modifier = Modifier.clipToBounds()) {
            AnimatedVisibility(
                visible = !isScrolled,
                enter = slideInVertically(initialOffsetY = { -it }, animationSpec = tween(durationMillis = 300)),
                exit = slideOutVertically(targetOffsetY = { -it }, animationSpec = tween(durationMillis = 300))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    RingChart(
                        spent = 4500f,
                        budget = 8000f,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DailyAverageSpendingCard(
                            dailyAverage = 853.0f,
                            previousMonthAverage = 1201.12f,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(Modifier.width(12.dp))

                        BalanceHighlightBox(
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Text(
                text = "Latest Transaction",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                color = MaterialTheme.colorScheme.inverseSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                state = listState,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(allTransactions) { transaction ->
                    SpendingCard(expenseItem = transaction)
                }
            }
        }
    }
}
