/*
 * Created by Saeedus Salehin on 15/5/25, 2:55 PM.
 */

package com.aiapp.flowcent.accounts.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UserAction
import com.aiapp.flowcent.accounts.presentation.components.AccountList
import com.aiapp.flowcent.accounts.presentation.components.NoAccountsYetScreen
import com.aiapp.flowcent.core.presentation.components.SearchBar

@Composable
fun AccountsHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel,
    state: AccountState
) {

    LaunchedEffect(key1 = Unit) {
        viewModel.onAction(UserAction.FetchUserUId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Shared Accounts",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            IconButton(onClick = { viewModel.onAction(UserAction.ClickAdd) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Account",
                    tint = Color.White,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(6.dp)
                )
            }
        }


        Spacer(Modifier.height(12.dp))

        // Search bar
        SearchBar(
            query = "",
            placeholder = "Search accounts...",
            onQueryChange = {}
        )

        if (state.accounts.isEmpty()) {
            AccountList(
                accounts = state.accounts,
                onAccountClick = { account ->
                    viewModel.onAction(UserAction.OnAccountItemClick(account))
                },
            )
        } else {
            Spacer(Modifier.height(16.dp))
            NoAccountsYetScreen(
                onAddAccountClick = { viewModel.onAction(UserAction.ClickAdd) },
                onLearnMoreClick = { }
            )
        }
    }

}
