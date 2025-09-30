/*
 * Created by Saeedus Salehin on 15/5/25, 2:51 PM.
 */

package com.aiapp.flowcent.home.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.core.Spacing
import com.aiapp.flowcent.core.presentation.components.AppOutlineButton
import com.aiapp.flowcent.core.presentation.components.NameInitial
import com.aiapp.flowcent.core.presentation.components.ShimmerBox
import com.aiapp.flowcent.core.presentation.components.ShimmerSpendingCard
import com.aiapp.flowcent.core.presentation.components.SpendingCard
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentDate
import com.aiapp.flowcent.home.presentation.HomeState
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.home.presentation.UserAction
import com.aiapp.flowcent.home.presentation.components.BalanceHighlightBox
import com.aiapp.flowcent.home.presentation.components.CalendarStrip
import com.aiapp.flowcent.home.presentation.components.InsightsHighlightBox
import com.aiapp.flowcent.home.presentation.components.RingChart
import com.aiapp.flowcent.home.util.calculateDailyActualSaving
import com.aiapp.flowcent.home.util.calculateDailySavingTarget
import com.aiapp.flowcent.home.util.dailyExpenseBudget
import com.aiapp.flowcent.subscription.presentation.PurchaseUserAction
import com.aiapp.flowcent.subscription.presentation.SubscriptionViewModel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.models.PurchasesError
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.ic_notification
import io.github.aakira.napier.Napier
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.painterResource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    homeState: HomeState,
    subscriptionVM: SubscriptionViewModel,
) {
    val subscriptionState by subscriptionVM.subscriptionState.collectAsState()
    val allTransactions = homeState.latestTransactions.flatten()
    val listState = rememberLazyListState()
    val isScrolled by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 } }

    val currentHour = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).hour
    val greeting = when (currentHour) {
        in 0..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        else -> "Good Evening"
    }


    LaunchedEffect(key1 = homeState.selectedDate) {
        homeViewModel.onAction(UserAction.FetchUserUId)
    }

    LaunchedEffect(key1 = homeState.user?.flowCentUserId) {
        fun showError(purchasesError: PurchasesError) {
            Napier.e("Sohan registerAppUserId purchasesError: $purchasesError")
        }
        if (homeState.user != null && homeState.user.flowCentUserId.isNotEmpty()) {
            Purchases.sharedInstance.logIn(
                homeState.user.flowCentUserId, ::showError
            ) { customerInfo, created ->

                //TODO - Make it run one time per one day 
                subscriptionVM.onAction(
                    PurchaseUserAction.UpdateCurrentPlan(
                        homeState.uid,
                        customerInfo,
                        true
                    )
                )
            }
        }
    }


    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.horizontalPadding),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { homeViewModel.onAction(UserAction.NavigateToProfile) }) {
                    NameInitial(
                        text = homeState.user?.localUserName ?: "TH",
                        textSize = 16.sp,
                        size = 36.dp
                    )
                }

                Spacer(Modifier.width(Spacing.small))

                Column {
                    Text(
                        text = greeting,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Text(
                        text = homeState.user?.localUserName ?: "User",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }


            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(Res.drawable.ic_notification),
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        CalendarStrip(
            selectedDate = getCurrentDate(),
            onDateSelected = {
                Napier.i("Sohan selected date: $it")
                homeViewModel.onAction(UserAction.SetSelectedDate(it))
            },
            modifier = Modifier.padding(horizontal = Spacing.horizontalPadding)
        )

        Spacer(Modifier.height(Spacing.mediumLarge))

        AnimatedVisibility(
            visible = !isScrolled,
            modifier = Modifier.padding(horizontal = Spacing.horizontalPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                if (homeState.isLoading) {
                    ShimmerBox(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Spacer(Modifier.height(Spacing.mediumLarge))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ShimmerBox(
                            modifier = Modifier
                                .weight(1f)
                                .height(132.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                        Spacer(Modifier.width(Spacing.mediumLarge))
                        ShimmerBox(
                            modifier = Modifier
                                .weight(1f)
                                .height(132.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth().animateEnterExit()
                    ) {
                        Spacer(Modifier.height(Spacing.medium))

                        RingChart(
                            actual = homeState.dailyTotalSpend.toFloat(),
                            target = dailyExpenseBudget(
                                homeState.userInitialBalance,
                                homeState.userSavingTarget
                            ).toFloat(),
                            dailyAverage = 853.0f,
                            previousMonthAverage = 1201.12f,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(Spacing.large))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BalanceHighlightBox(
                                totalSpend = homeState.userTotalSpent.toString(),
                                totalIncome = homeState.userTotalIncome.toString(),
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(Modifier.width(Spacing.large))

                            InsightsHighlightBox(
                                actual = calculateDailyActualSaving(
                                    homeState.dailyTotalSpend,
                                    homeState.userInitialBalance,
                                    homeState.userSavingTarget
                                ),
                                target = calculateDailySavingTarget(homeState.userSavingTarget),
                                modifier = Modifier.weight(1f),
                                onClicked = {
                                    homeViewModel.onAction(UserAction.NavigateToInsights)
                                }
                            )
                        }
                    }
                }
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.padding(horizontal = Spacing.horizontalPadding)
        ) {
            stickyHeader(key = "latest-transaction-header") {
                Text(
                    text = "Transactions",
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                    color = MaterialTheme.colorScheme.inverseSurface,
                    modifier = Modifier.fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(vertical = Spacing.verticalPadding)
                        .padding(top = 24.dp)
                )
            }

            if (homeState.isLoading) {
                items(5) {
                    ShimmerSpendingCard(modifier = Modifier.animateItem())
                }
            } else {
                item {
                    if (allTransactions.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Spacer(modifier = Modifier.height(32.dp))
                            Text(
                                text = "You don't have recorded any transaction yet.",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(48.dp))

                            AppOutlineButton(
                                text = "Let's Dive In",
                                onClick = {
                                    homeViewModel.onAction(UserAction.NavigateToChat)
                                },
                                hasGradient = false,
                                outlineColor = MaterialTheme.colorScheme.primary,
                                textColor = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
                items(allTransactions, key = { "fct-${Uuid.random()}" }) { transaction ->
                    SpendingCard(
                        expenseItem = transaction,
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}
