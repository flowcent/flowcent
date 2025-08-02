package com.aiapp.flowcent.accounts.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UiEvent
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavRoutes

@Composable
fun Events(
    accountViewModel: AccountViewModel,
    globalNavController: NavController,
    localNavController: NavController
) {
    LaunchedEffect(Unit) {
        accountViewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.ClickAdd -> {
                    localNavController.navigate(AccountsNavRoutes.AddAccountScreen.route)
                }

                UiEvent.NavigateToAccountDetail -> {
                    localNavController.navigate(AccountsNavRoutes.AccountDetailScreen.route)
                }
            }
        }
    }
}