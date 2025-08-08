package com.aiapp.flowcent.chat.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.UiEvent
import com.aiapp.flowcent.chat.presentation.UserAction
import com.aiapp.flowcent.chat.presentation.components.AccountSelectionToggle
import com.aiapp.flowcent.chat.presentation.components.BotMessage
import com.aiapp.flowcent.chat.presentation.components.ChatInput
import com.aiapp.flowcent.chat.presentation.components.PromptSave
import com.aiapp.flowcent.chat.presentation.components.SpendingList
import com.aiapp.flowcent.chat.presentation.components.UserMessage
import com.aiapp.flowcent.core.presentation.permission.FCPermissionState
import com.aiapp.flowcent.core.presentation.platform.SpeechRecognizer
import com.aiapp.flowcent.core.presentation.permission.PermissionsViewModel
import dev.icerock.moko.permissions.PermissionState
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.outline_charger
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource


@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    chatState: ChatState,
    viewModel: ChatViewModel,
    speechRecognizer: SpeechRecognizer,
    permissionsVM: PermissionsViewModel,
    fcPermissionsState: FCPermissionState,
    localNavController: NavController,
    globalNavController: NavController
) {

    var hasAudioPermission: Boolean by remember { mutableStateOf(false) }
    var isListening by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val accounts = listOf("Flat B1", "51/L")

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

    LaunchedEffect(Unit) {
        viewModel.onAction(UserAction.CheckAudioPermission)
        viewModel.onAction(UserAction.FetchUserUId)
    }

//    LaunchedEffect(Unit) {
//        if (!speechRecognizer.isRecognitionAvailable()) {
//            println("Sohan Speech recognition not available on this device")
//        } else {
//            println("Sohan Speech recognition is available on this device")
//        }
//    }


    LaunchedEffect(key1 = rememberScaffoldState()) {
        viewModel.uiEvent.collect {
            when (it) {
                is UiEvent.ShowSnackBar -> {}
                UiEvent.StartAudioPlayer -> {
                    if (hasAudioPermission) {
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
                    permissionsVM.provideOrRequestRecordAudioPermission()
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AccountSelectionToggle(
                selectionType = chatState.selectionType,
                onSelectionChanged = {
                    viewModel.onAction(UserAction.UpdateAccountSelectionType(it))
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true
        ) {
            println("Sohan messages: ${chatState.messages}")
            items(chatState.messages.reversed()) {
                if (it.isUser) {
                    UserMessage(text = it.text)
                } else {
                    Column {
                        if (it.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.CenterHorizontally),
                                color = Color.Blue
                            )
                        } else {
                            Row(
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.outline_charger),
                                    contentDescription = null,
                                    tint = Color.Black,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))

                                Column(
                                    modifier = Modifier
                                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                                        .padding(16.dp)
                                ) {
                                    BotMessage(text = it.text, isRich = true)
                                    if (it.expenseItems.isNotEmpty()) {
                                        SpendingList(
                                            it.expenseItems,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                        PromptSave(
                                            onClickSave = {
                                                viewModel.onAction(
                                                    UserAction.SaveExpenseItemsToDb(
                                                        it.expenseItems
                                                    )
                                                )
                                            },
                                            onClickClose = {
                                                viewModel.onAction(UserAction.DiscardExpenseItems)
                                            }
                                        )
                                    }

                                    if (chatState.showAccounts) {
                                        LazyRow(
                                            modifier = Modifier.padding(vertical = 8.dp),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            items(accounts) {
                                                Box(
                                                    modifier = Modifier.size(100.dp)
                                                        .padding(8.dp)
                                                        .background(Color.Yellow)
                                                ) {
                                                    Text(
                                                        text = it,
                                                        color = Color.White,
                                                        modifier = Modifier.align(Alignment.Center)
                                                    )
                                                }
                                            }
                                        }
                                    }
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





