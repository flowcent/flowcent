package com.aiapp.flowcent.chat.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.UiEvent
import com.aiapp.flowcent.chat.presentation.UserAction
import com.aiapp.flowcent.chat.presentation.components.BotMessage
import com.aiapp.flowcent.chat.presentation.components.ChatInput
import com.aiapp.flowcent.chat.presentation.components.SpendingCard
import com.aiapp.flowcent.chat.presentation.components.UserMessage
import com.aiapp.flowcent.core.presentation.PermissionsViewModel
import com.aiapp.flowcent.core.presentation.ui.theme.AppTheme
import com.aiapp.flowcent.core.platform.SpeechRecognizer
import dev.icerock.moko.permissions.PermissionState
import kotlinx.coroutines.launch


@Composable
fun ChatScreen(
    chatState: ChatState,
    viewModel: ChatViewModel,
    speechRecognizer: SpeechRecognizer,
    permissionsVM: PermissionsViewModel
) {

    var hasPermission: Boolean by remember { mutableStateOf(false) }
    var isListening by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = permissionsVM.state) {
        hasPermission = when (permissionsVM.state) {
            PermissionState.NotDetermined -> false

            PermissionState.NotGranted -> false

            PermissionState.Granted -> true

            PermissionState.Denied -> false

            PermissionState.DeniedAlways -> false

            null -> false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onAction(UserAction.CheckAudioPermission)
    }

    LaunchedEffect(Unit) {
        if (!speechRecognizer.isRecognitionAvailable()) {
            println("Sohan Speech recognition not available on this device")
        } else {
            println("Sohan Speech recognition is available on this device")
        }
    }



    LaunchedEffect(key1 = rememberScaffoldState()) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.ShowSnackBar -> {}
                UiEvent.StartAudioPlayer -> {
                    if (hasPermission) {
                        coroutineScope.launch {
                            speechRecognizer.startListening().collect { text ->
                                viewModel.onAction(UserAction.UpdateText(text))
                            }
                            isListening = false
                        }
                        isListening = true
                    } else {
                        println("Sohan Microphone permission denied. Please enable it in app settings.")
                    }
                }

                UiEvent.StopAudioPlayer -> {
                    if (isListening) {
                        speechRecognizer.stopListening()
                        isListening = false
                    }
                }

                UiEvent.CheckAudioPermission -> {
                    permissionsVM?.provideOrRequestRecordAudioPermission()
                }
            }
        }
    }

    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            LazyColumn(
                modifier = Modifier.weight(1f),
                reverseLayout = true
            ) {
                items(chatState.messages.reversed()) {
                    if (it.isUser) {
                        UserMessage(text = it.text)
                    } else {
                        if (chatState.isCircularLoading && chatState.messages[chatState.messages.lastIndex].isUser.not()) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                color = Color(0xFFFFFFFF)
                            )
                        } else {
                            Column {
                                BotMessage(text = it.text)
                                if (it.expenseItems.isNotEmpty()) {
                                    it.expenseItems.forEach { expenseItem ->
                                        SpendingCard(expenseItem)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            ChatInput(
                state = chatState,
                isListening = isListening,
                onUpdateText = {
                    viewModel.onAction(UserAction.UpdateText(it))
                },
                onClickMic = {
                    if (isListening) {
                        viewModel.onAction(UserAction.StopAudioPlayer)
                    } else {
                        viewModel.onAction(UserAction.StartAudioPlayer)
                    }
                }
            ) {
                viewModel.onAction(UserAction.SendMessage(chatState.userText))
            }
        }
    }
}





