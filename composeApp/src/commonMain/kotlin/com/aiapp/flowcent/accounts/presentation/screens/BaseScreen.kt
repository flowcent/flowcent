package com.aiapp.flowcent.accounts.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UiEvent
import com.aiapp.flowcent.accounts.presentation.UserAction
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavRoutes
import com.aiapp.flowcent.auth.presentation.event.EventHandler
import com.aiapp.flowcent.core.presentation.components.NoInternet
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.platform.ConnectivityObserver
import com.aiapp.flowcent.core.presentation.platform.rememberConnectivityObserver
import com.aiapp.flowcent.core.utils.DialogType

@Composable
fun BaseScreen(
    navController: NavController,
    viewModel: AccountViewModel,
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier, viewModel: AccountViewModel, state: AccountState) -> Unit
) {
    val connectivityObserver = rememberConnectivityObserver()

    val status by connectivityObserver.observe()
        .collectAsState(initial = ConnectivityObserver.Status.Initializing)

    val isMobileData by connectivityObserver.isMobileDataEnabled()
        .collectAsState(initial = false)

    LaunchedEffect(key1 = status) {
        viewModel.onAction(UserAction.CheckInternet(status = status))
    }

    var showDialog by remember { mutableStateOf<UiEvent.ShowDialog?>(null) }

    EventHandler(eventFlow = viewModel.uiEvent) { event ->
        handleEvent(event, navController) { dialogEvent ->
            showDialog = dialogEvent
        }
    }

    val state by viewModel.state.collectAsState()

    showDialog?.let {
        Dialog(
            onDismissRequest = { showDialog = null },
        ) {
            if (it.dialogType == DialogType.NO_INTERNET) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(300.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))

                ) {
                    NoInternet(
                        imageSize = 100.dp,
                        onButtonClick = { showDialog = null }
                    )
                }
            }
        }
    }

    content(modifier, viewModel, state)
}

private fun handleEvent(
    event: UiEvent,
    navController: NavController,
    onShowDialog: (UiEvent.ShowDialog) -> Unit
) {
    when (event) {
        UiEvent.ClickAdd -> navController.navigate(AccountsNavRoutes.AddAccountScreen.route)
        UiEvent.NavigateToAccountDetail -> navController.navigate(AccountsNavRoutes.AccountDetailScreen.route)
        UiEvent.NavigateToAccountHome -> navController.navigate(AccountsNavRoutes.AccountsHomeScreen.route)
        UiEvent.NavigateToChat -> navController.navigate(AppNavRoutes.Chat.route)
        is UiEvent.ShowDialog -> onShowDialog(event)
    }
}