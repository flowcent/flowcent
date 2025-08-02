package com.aiapp.flowcent.accounts.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import io.github.aakira.napier.Napier

@Composable
fun AccountDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel,
    state: AccountState,
    localNavController: NavController,
    globalNavController: NavController
) {
    Napier.e("Sohan account detail: ${state.selectedAccount}")

}