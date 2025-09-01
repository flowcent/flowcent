package com.aiapp.flowcent.accounts.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.accounts.presentation.randomColor

@Composable
fun AccountList(
    accounts: List<Account>,
    onAccountClick: (Account) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Spacer(Modifier.height(16.dp))

        // Account Cards
        LazyColumn {
            items(accounts) { account ->
                AccountCard(
                    account = account,
                    totalMonthlyExpense = 0.0,
                    budget = account.initialBalance.toString(),
                    progress = 0.85f,
                    progressColor = randomColor(),
                    onClickItem = {
                        onAccountClick(account)
                    }
                )
            }

            item {
                Spacer(Modifier.height(12.dp))
            }
        }
    }
}