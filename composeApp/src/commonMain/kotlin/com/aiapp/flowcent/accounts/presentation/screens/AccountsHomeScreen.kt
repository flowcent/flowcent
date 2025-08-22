/*
 * Created by Saeedus Salehin on 15/5/25, 2:55 PM.
 */

package com.aiapp.flowcent.accounts.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UserAction
import com.aiapp.flowcent.accounts.presentation.components.AccountCard
import com.aiapp.flowcent.accounts.presentation.components.AvatarGroup
import com.aiapp.flowcent.accounts.presentation.components.SelectableIcon
import com.aiapp.flowcent.accounts.presentation.getAccountIcon
import com.aiapp.flowcent.accounts.presentation.randomColor
import com.aiapp.flowcent.accounts.presentation.randomGradient
import com.aiapp.flowcent.core.presentation.components.AnimatedRoundedProgressBar
import com.aiapp.flowcent.core.presentation.components.NoAccountCard
import com.aiapp.flowcent.core.presentation.components.SearchBar
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

        Spacer(Modifier.height(16.dp))

        // Account Cards
        LazyColumn {
            items(state.accounts) { account ->
                AccountCard(
                    account = account,
                    totalMonthlyExpense = 0.0,
                    budget = account.initialBalance.toString(),
                    progress = 0.85f,
                    progressColor = randomColor(),
                    onClickItem = {
                        viewModel.onAction(
                            UserAction.OnAccountItemClick(
                                account
                            )
                        )
                    }
                )
            }

            item {
                Spacer(Modifier.height(12.dp))
            }
        }
    }

}
