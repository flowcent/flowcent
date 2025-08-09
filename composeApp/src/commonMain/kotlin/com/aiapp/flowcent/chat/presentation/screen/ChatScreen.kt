package com.aiapp.flowcent.chat.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Icon
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
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.chat.domain.model.AccountSelectionType
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.UiEvent
import com.aiapp.flowcent.chat.presentation.UserAction
import com.aiapp.flowcent.chat.presentation.components.AccountSelectorRow
import com.aiapp.flowcent.chat.presentation.components.AccountTypeSelectionToggle
import com.aiapp.flowcent.chat.presentation.components.BotMessage
import com.aiapp.flowcent.chat.presentation.components.ChatInput
import com.aiapp.flowcent.chat.presentation.components.PromptSave
import com.aiapp.flowcent.chat.presentation.components.SpendingList
import com.aiapp.flowcent.chat.presentation.components.UserMessage
import com.aiapp.flowcent.core.presentation.permission.FCPermissionState
import com.aiapp.flowcent.core.presentation.permission.PermissionsViewModel
import com.aiapp.flowcent.core.presentation.platform.SpeechRecognizer
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

    val sampleAccounts = listOf(
        Account(
            id = "ACC001",
            accountId = "ACC001",
            accountName = "Travel Fund",
            profileImage = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/50/Bitcoin.png/600px-Bitcoin.png",
        ),
        Account(
            id = "ACC002",
            accountId = "ACC002",
            accountName = "Savings",
            profileImage = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/46/Euro_coin_1_Euro_obverse.png/600px-Euro_coin_1_Euro_obverse.png",
        )
    )


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

    LaunchedEffect(key1 = chatState.messages) {
        if (chatState.messages.isNotEmpty() && chatState.messages.last().isUser.not()) {
            viewModel.onAction(UserAction.UpdateAllCheckedItems(chatState.messages.last().expenseItems))
        }
    }


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
        AccountTypeSelectionToggle(
            selectionType = chatState.selectionType,
            onSelectionChanged = {
                viewModel.onAction(UserAction.UpdateAccountSelectionType(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        )

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
                        if (it.isBotMessageLoading) {
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
                                            expenseItems = it.expenseItems,
                                            checkedExpenseItems = chatState.checkedExpenseItems,
                                            onCheckedItem = { isChecked, expenseItem ->
                                                viewModel.onAction(
                                                    UserAction.UpdateCheckedItem(
                                                        isChecked,
                                                        expenseItem
                                                    )
                                                )
                                            },
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )

                                        if (chatState.selectionType == AccountSelectionType.SHARED &&
                                            chatState.sharedAccounts.isNotEmpty()
                                        ) {
                                            AccountSelectorRow(
                                                accounts = chatState.sharedAccounts,
                                                selectedAccountId = chatState.selectedAccountId,
                                                onAccountSelected = { account ->
                                                    viewModel.onAction(
                                                        UserAction.SelectAccount(
                                                            account.id,
                                                            account.accountName
                                                        )
                                                    )
                                                }
                                            )
                                        }

                                        PromptSave(
                                            selectionType = chatState.selectionType,
                                            selectedAccountName = chatState.selectedAccountName,
                                            onClickSave = {
                                                viewModel.onAction(
                                                    UserAction.SaveExpenseItemsToDb
                                                )
                                            },
                                            onClickDiscard = {
                                                viewModel.onAction(UserAction.DiscardExpenseItems)
                                            }
                                        )
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





