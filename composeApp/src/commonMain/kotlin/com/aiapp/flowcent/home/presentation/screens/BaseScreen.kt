package com.aiapp.flowcent.home.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.aiapp.flowcent.core.presentation.components.NoInternet
import com.aiapp.flowcent.core.utils.DialogType
import com.aiapp.flowcent.home.presentation.HomeState
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.home.presentation.UiEvent
import com.aiapp.flowcent.home.presentation.event.EventHandler
import com.aiapp.flowcent.util.ConnectivityManager

@Composable
fun BaseScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier, viewModel: HomeViewModel, state: HomeState) -> Unit
) {

    val state by viewModel.state.collectAsState()

    var showDialog by remember { mutableStateOf<UiEvent.ShowDialog?>(null) }
    val isNetworkConnected by ConnectivityManager.connectivityStatus.collectAsState()

    EventHandler(eventFlow = viewModel.uiEvent) { event ->
        handleEvent(event, navController) { dialogEvent ->
            showDialog = dialogEvent
        }
    }

    LaunchedEffect(key1 = isNetworkConnected) {
        showDialog = if (isNetworkConnected) {
            null
        } else {
            UiEvent.ShowDialog(
                title = "No Internet",
                body = "Please check your internet connection",
                dialogType = DialogType.NO_INTERNET
            )
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
                        .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp))

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

