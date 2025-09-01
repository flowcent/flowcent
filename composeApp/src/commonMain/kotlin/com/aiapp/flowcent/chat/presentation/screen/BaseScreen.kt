package com.aiapp.flowcent.chat.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.UiEvent
import com.aiapp.flowcent.chat.presentation.UserAction
import com.aiapp.flowcent.chat.presentation.components.ChatInput
import com.aiapp.flowcent.chat.presentation.event.EventHandler
import com.aiapp.flowcent.chat.presentation.navigation.ChatNavRoutes
import com.aiapp.flowcent.core.presentation.components.NoInternet
import com.aiapp.flowcent.core.presentation.permission.FCPermissionState
import com.aiapp.flowcent.core.presentation.permission.PermissionsViewModel
import com.aiapp.flowcent.core.presentation.platform.SpeechRecognizer
import com.aiapp.flowcent.core.utils.DialogType
import com.aiapp.flowcent.subscription.presentation.SubscriptionState
import com.aiapp.flowcent.subscription.presentation.SubscriptionViewModel
import com.aiapp.flowcent.util.ConnectivityManager
import dev.icerock.moko.permissions.PermissionState
import kotlinx.coroutines.launch

@Composable
fun BaseScreen(
    navController: NavHostController,
    viewModel: ChatViewModel,
    speechRecognizer: SpeechRecognizer,
    permissionsVM: PermissionsViewModel,
    fcPermissionsState: FCPermissionState,
    subscriptionVM: SubscriptionViewModel,
    modifier: Modifier = Modifier,
    content: @Composable (
        modifier: Modifier,
        viewModel: ChatViewModel,
        state: ChatState,
        subscriptionState: SubscriptionState
    ) -> Unit
) {
    val state by viewModel.chatState.collectAsState()

    val subscriptionState by subscriptionVM.subscriptionState.collectAsState()

    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    var hasAudioPermission: Boolean by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    var showDialog by remember { mutableStateOf<UiEvent.ShowDialog?>(null) }
    val isNetworkConnected by ConnectivityManager.connectivityStatus.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(UserAction.CheckAudioPermission)
    }

    LaunchedEffect(key1 = fcPermissionsState.audioPermissionState) {
        hasAudioPermission = when (fcPermissionsState.audioPermissionState) {
            PermissionState.NotDetermined -> false
            PermissionState.NotGranted -> false
            PermissionState.Granted -> true
            PermissionState.Denied -> false
            PermissionState.DeniedAlways -> false
            null -> false
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


    fun handleEvent(
        event: UiEvent,
        navController: NavController,
        onShowDialog: (UiEvent.ShowDialog) -> Unit
    ) {
        when (event) {
            UiEvent.CheckAudioPermission -> {
                permissionsVM.provideOrRequestRecordAudioPermission()
            }

            is UiEvent.ShowDialog -> onShowDialog(event)
            is UiEvent.ShowSnackBar -> {}
            is UiEvent.StartAudioPlayer -> {
                if (hasAudioPermission) {
                    coroutineScope.launch {
                        speechRecognizer.startListening().collect { text ->
                            viewModel.onAction(UserAction.UpdateVoiceText(text))
                        }
                        viewModel.onAction(UserAction.UpdateListening(false))
                    }
                    viewModel.onAction(UserAction.UpdateListening(true))
                } else {
                    println("Sohan Microphone permission denied. Please enable it in app settings.")
                }
            }

            UiEvent.StopAudioPlayer -> {
                if (state.isListening) {
                    speechRecognizer.stopListening()
                    viewModel.onAction(UserAction.UpdateListening(false))
                }
            }

            UiEvent.NavigateToChat -> navController.navigate(ChatNavRoutes.ChatListScreen.route)
            UiEvent.NavigateToBack -> navController.popBackStack()
        }
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
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        bottomBar = {
            if (currentRoute == ChatNavRoutes.ChatListScreen.route) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ChatInput(
                        state = state,
                        isListening = state.isListening,
                        onUpdateText = {
                            viewModel.onAction(UserAction.UpdateText(it))
                        },
                        onClickMic = {

                        }
                    ) {
                        viewModel.onAction(UserAction.SendMessage(state.userText))
                    }
                }
            }
        }
    ) {
        content(modifier, viewModel, state, subscriptionState)
    }
}

