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
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.auth.presentation.event.EventHandler
import com.aiapp.flowcent.auth.presentation.navigation.AuthNavRoutes
import com.aiapp.flowcent.core.presentation.components.NoInternet
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.platform.ConnectivityObserver
import com.aiapp.flowcent.core.presentation.platform.rememberConnectivityObserver
import com.aiapp.flowcent.core.utils.DialogType

@Composable
fun BaseScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier, viewModel: AuthViewModel, state: AuthState) -> Unit
) {
    val connectivityObserver = rememberConnectivityObserver()

    // Observe general connectivity
    val status by connectivityObserver.observe()
        .collectAsState(initial = ConnectivityObserver.Status.Initializing)

    // Observe mobile data status
    val isMobileData by connectivityObserver.isMobileDataEnabled()
        .collectAsState(initial = false)

    LaunchedEffect(key1 = status) {
        viewModel.onAction(UserAction.CheckInternet(status = status))
    }

    // You can manage other shared states here, like a global dialog
    var showDialog by remember { mutableStateOf<UiEvent.ShowDialog?>(null) }

    // The EventHandler is now centralized in this single location
    EventHandler(eventFlow = viewModel.uiEvent) { event ->
        handleEvent(event, navController) { dialogEvent ->
            showDialog = dialogEvent
        }
    }

    val state by viewModel.state.collectAsState()


    // Handle showing the dialog here
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
    }
}