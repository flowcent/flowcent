/*
 * Created by Saeedus Salehin on 15/5/25, 2:55 PM.
 */

package com.aiapp.flowcent.accounts.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UserAction
import com.aiapp.flowcent.accounts.presentation.components.AccountCard
import com.aiapp.flowcent.core.presentation.components.NoAccountCard
import io.github.aakira.napier.Napier

@Composable
fun AccountsHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel,
    state: AccountState
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.onAction(UserAction.FetchUserUId)
    }

    Napier.e("Sohan accounts ${state.accounts}")

    Box(
        modifier = modifier.fillMaxSize().padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                if (state.accounts.isEmpty()) {
                    NoAccountCard(
                        onClick = { viewModel.onAction(UserAction.ClickAdd) }
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        stickyHeader(key = "my-accounts-header") {
                            Text(
                                text = "Groups",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.inverseSurface,
                                modifier = Modifier.fillMaxWidth()
                                    .background(color = MaterialTheme.colorScheme.background)
                                    .padding(vertical = 8.dp)
                            )
                        }

                        items(state.accounts) { account ->
                            AccountCard(
                                account = account,
                                onClickItem = {
                                    viewModel.onAction(
                                        UserAction.OnAccountItemClick(
                                            account
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        if (state.accounts.isNotEmpty()) {
            FloatingActionButton(
                onClick = { viewModel.onAction(UserAction.ClickAdd) },
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

}