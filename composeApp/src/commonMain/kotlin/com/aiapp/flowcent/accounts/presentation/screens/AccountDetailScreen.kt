package com.aiapp.flowcent.accounts.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.accounts.domain.model.AccountMember
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UserAction
import com.aiapp.flowcent.core.presentation.components.SpendingCard
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentDate
import com.aiapp.flowcent.home.presentation.components.CalendarStrip
import com.aiapp.flowcent.home.presentation.components.RingChart

sealed class MemberTab {
    data object Latest : MemberTab()
    data class Member(val accountMember: AccountMember) : MemberTab()
}

@Composable
fun AccountDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel,
    state: AccountState
) {
    val listState = rememberLazyListState()
    val isScrolled by remember { derivedStateOf { listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0 } }

    val members = state.selectedAccount?.members.orEmpty()

    val reorderedMembers = members
        .sortedWith(compareByDescending { it.memberId == state.uid })

    // Track current tab
    var currentTab by remember { mutableStateOf<MemberTab>(MemberTab.Latest) }

    // Compute tab index (Latest is index 0)
    val selectedTabIndex = when (currentTab) {
        is MemberTab.Latest -> 0
        is MemberTab.Member -> reorderedMembers.indexOf((currentTab as MemberTab.Member).accountMember) + 1
    }

    val flattenedExpenseItems = state.latestTransactions.flatMap { transaction ->
        transaction.expenses.map { expenseItem ->
            transaction.creatorUserName to expenseItem
        }
    }


    fun onLatestTabClick(onTabChange: (MemberTab) -> Unit) {
        onTabChange(MemberTab.Latest)
    }

    fun onMemberTabClick(member: AccountMember, onTabChange: (MemberTab) -> Unit) {
        onTabChange(MemberTab.Member(member))
        viewModel.onAction(UserAction.GetUsersDailyTransaction(member.memberId))
    }

    LaunchedEffect(key1 = state.selectedDate) {
        viewModel.onAction(UserAction.GetDailyTransactions)
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            CalendarStrip(
                selectedDate = getCurrentDate(),
                onDateSelected = { viewModel.onAction(UserAction.SetSelectedDate(it)) },
                modifier = Modifier.padding(16.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            viewModel.onAction(UserAction.NavigateToChat)
                        }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    ScrollableTabRow(
                        selectedTabIndex = selectedTabIndex,
                        modifier = Modifier.weight(1f),
                        edgePadding = 0.dp,
                        containerColor = Color.Transparent,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                            )
                        },
                        divider = {}) {
                        // First tab: Latest
                        Tab(
                            selected = currentTab is MemberTab.Latest,
                            onClick = { onLatestTabClick { currentTab = it } },
                            text = {
                                Text(
                                    "Latest",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            })

                        // Other tabs: members
                        reorderedMembers.forEach { member ->
                            Tab(
                                selected = (currentTab as? MemberTab.Member)?.accountMember == member,
                                onClick = { onMemberTabClick(member) { currentTab = it } },
                                text = {
                                    Text(
                                        text = if (member.memberId == state.uid) "My Transaction" else member.memberLocalUserName,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            )
                        }
                    }
                }

                Divider(
                    thickness = 1.dp, color = Color.LightGray, modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier.clipToBounds().padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                AnimatedVisibility(
                    visible = !isScrolled, enter = slideInVertically(
                        initialOffsetY = { -it }, animationSpec = tween(durationMillis = 300)
                    ), exit = slideOutVertically(
                        targetOffsetY = { -it }, animationSpec = tween(durationMillis = 300)
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
                    ) {
                        RingChart(
                            actual = 4500f, target = 8000f, modifier = Modifier.fillMaxWidth(),
                            dailyAverage = 853.0f, previousMonthAverage = 1201.12f
                        )

                        Spacer(Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Spacer(Modifier.width(12.dp))

//                            BalanceHighlightBox(
//                                modifier = Modifier.weight(1f)
//                            )
                        }
                    }
                }
            }


            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                stickyHeader(key = "latest-transaction-header") {
                    Text(
                        text = "Transactions",
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                        color = MaterialTheme.colorScheme.inverseSurface,
                        modifier = Modifier.fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(vertical = 8.dp)
                    )
                }

                if (flattenedExpenseItems.isNotEmpty()) {
                    items(flattenedExpenseItems) { (createdBy, expenseItem) ->
                        SpendingCard(
                            createdBy = createdBy,
                            expenseItem = expenseItem,
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }


        }

        FloatingActionButton(
            onClick = { viewModel.onAction(UserAction.NavigateToChat) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Account",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}