package com.aiapp.flowcent.auth.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UiEvent
import com.aiapp.flowcent.auth.presentation.event.EventHandler
import com.aiapp.flowcent.auth.presentation.navigation.AuthNavRoutes
import com.aiapp.flowcent.core.presentation.components.NoInternet
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.utils.DialogType
import com.aiapp.flowcent.util.ConnectivityManager

@Composable
fun BaseScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier, viewModel: AuthViewModel, state: AuthState) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val isNetworkConnected by ConnectivityManager.connectivityStatus.collectAsState()

    var showDialog by remember { mutableStateOf<UiEvent.ShowDialog?>(null) }

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


    // Handle showing the dialog here
    showDialog?.let {
        Dialog(
            onDismissRequest = { showDialog = null },
        ) {
            if (it.dialogType == DialogType.NO_INTERNET) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(300.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(12.dp)
                        )

                ) {
                    NoInternet(
                        imageSize = 100.dp,
                        onButtonClick = { showDialog = null }
                    )
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(150.dp)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(20.dp)

                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(it.title, fontStyle = MaterialTheme.typography.titleMedium.fontStyle)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(it.body, fontStyle = MaterialTheme.typography.bodyMedium.fontStyle)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Ok",
                            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                            color = Color.Red,
                            modifier = Modifier.clickable { showDialog = null })
                    }
                }
            }
        }
    }

    // Pass the state and event handler down to the specific screen content
    content(modifier, viewModel, state)
}

// Helper function to keep the 'when' statement clean
private fun handleEvent(
    event: UiEvent,
    navController: NavController,
    onShowDialog: (UiEvent.ShowDialog) -> Unit
) {
    when (event) {
        UiEvent.NavigateToHome -> navController.navigate(AppNavRoutes.Home.route)
        UiEvent.NavigateToBasicIntro -> navController.navigate(AuthNavRoutes.BasicIntroScreen.route)
        UiEvent.NavigateToSignIn -> navController.navigate(AuthNavRoutes.SignInScreen.route)
        UiEvent.NavigateToSignUp -> navController.navigate(AuthNavRoutes.SignUpScreen.route)
        is UiEvent.ShowDialog -> onShowDialog(event)
        UiEvent.HideDialog -> {}
    }
}