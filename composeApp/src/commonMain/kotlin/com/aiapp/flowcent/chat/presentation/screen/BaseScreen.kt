package com.aiapp.flowcent.chat.presentation.screen

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.UiEvent
import com.aiapp.flowcent.chat.presentation.UserAction
import com.aiapp.flowcent.chat.presentation.event.EventHandler
import com.aiapp.flowcent.chat.presentation.navigation.ChatNavRoutes
import com.aiapp.flowcent.core.presentation.components.NoInternet
import com.aiapp.flowcent.core.presentation.permission.FCPermissionState
import com.aiapp.flowcent.core.presentation.permission.PermissionsViewModel
import com.aiapp.flowcent.core.presentation.platform.SpeechRecognizer
import com.aiapp.flowcent.core.utils.DialogType
import com.aiapp.flowcent.util.ConnectivityManager
import dev.icerock.moko.permissions.PermissionState
import kotlinx.coroutines.launch

@Composable
fun BaseScreen(
    navController: NavController,
    viewModel: ChatViewModel,
    speechRecognizer: SpeechRecognizer,
    permissionsVM: PermissionsViewModel,
    fcPermissionsState: FCPermissionState,
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier, viewModel: ChatViewModel, state: ChatState) -> Unit
) {
    val state by viewModel.chatState.collectAsState()

    var hasAudioPermission: Boolean by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf<UiEvent.ShowDialog?>(null) }
    val isNetworkConnected by ConnectivityManager.connectivityStatus.collectAsState()

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
                            viewModel.onAction(UserAction.UpdateVoiceText(text, text))
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

            UiEvent.NavigateToVoice -> navController.navigate(ChatNavRoutes.VoiceAssistantScreen.route)
            UiEvent.NavigateToChat -> navController.navigate(ChatNavRoutes.ChatScreen.route)
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

