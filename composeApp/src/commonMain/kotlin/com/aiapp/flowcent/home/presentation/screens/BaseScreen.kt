package com.aiapp.flowcent.home.presentation.screens

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
import com.aiapp.flowcent.core.presentation.components.NoInternet
import com.aiapp.flowcent.core.presentation.platform.ConnectivityObserver
import com.aiapp.flowcent.core.presentation.platform.rememberConnectivityObserver
import com.aiapp.flowcent.core.utils.DialogType
import com.aiapp.flowcent.home.presentation.HomeState
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.home.presentation.UiEvent
import com.aiapp.flowcent.home.presentation.event.EventHandler

@Composable
fun BaseScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier, viewModel: HomeViewModel, state: HomeState) -> Unit
) {
    val connectivityObserver = rememberConnectivityObserver()

    val state by viewModel.state.collectAsState()

    val status by connectivityObserver.observe()
        .collectAsState(initial = ConnectivityObserver.Status.Initializing)

    val isMobileData by connectivityObserver.isMobileDataEnabled()
        .collectAsState(initial = false)

    var showDialog by remember { mutableStateOf<UiEvent.ShowDialog?>(null) }

    LaunchedEffect(key1 = status) {
//        viewModel.onAction(UserAction.CheckInternet(status = status))
    }


    EventHandler(eventFlow = viewModel.uiEvent) { event ->
        handleEvent(event, navController) { dialogEvent ->
            showDialog = dialogEvent
        }
    }

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
        is UiEvent.Navigate -> navController.navigate(event.route)
        is UiEvent.ShowDialog -> onShowDialog(event)
        is UiEvent.ShowSnackbar -> {}
    }
}

