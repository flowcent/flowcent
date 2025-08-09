package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.accounts.domain.model.Account

@Composable
fun AccountSelectorRow(
    accounts: List<Account>,
    selectedAccountId: String?,
    onAccountSelected: (Account) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        itemsIndexed(accounts) { _, account ->
            AccountOptionCard(
                title = account.accountName,
                selected = selectedAccountId == account.id,
                onSelected = { onAccountSelected(account) }
            )
        }
    }
}
