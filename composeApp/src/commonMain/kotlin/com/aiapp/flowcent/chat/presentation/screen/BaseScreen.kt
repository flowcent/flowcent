package com.aiapp.flowcent.chat.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavRoutes
import com.aiapp.flowcent.chat.presentation.ChatState
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.UiEvent
import com.aiapp.flowcent.chat.presentation.UserAction
import com.aiapp.flowcent.chat.presentation.components.AccountSelectorRow
import com.aiapp.flowcent.chat.presentation.components.ChatInput
import com.aiapp.flowcent.chat.presentation.event.EventHandler
import com.aiapp.flowcent.chat.presentation.navigation.ChatNavRoutes
import com.aiapp.flowcent.core.presentation.components.AppButton
import com.aiapp.flowcent.core.presentation.components.NoInternet
import com.aiapp.flowcent.core.presentation.permission.FCPermissionState
import com.aiapp.flowcent.core.presentation.permission.PermissionsViewModel
import com.aiapp.flowcent.core.presentation.platform.SpeechRecognizer
import com.aiapp.flowcent.core.utils.DialogType
import com.aiapp.flowcent.subscription.presentation.PurchaseUserAction
import com.aiapp.flowcent.subscription.presentation.SubscriptionState
import com.aiapp.flowcent.subscription.presentation.SubscriptionViewModel
import com.aiapp.flowcent.subscription.presentation.component.RcPaywall
import com.aiapp.flowcent.util.ConnectivityManager
import dev.icerock.moko.permissions.PermissionState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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

    val subscriptionSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val accountSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)


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

    LaunchedEffect(key1 = state.showSubscriptionSheet) {
        if (state.showSubscriptionSheet) {
            coroutineScope.launch {
                subscriptionSheetState.show()
            }
        } else {
            coroutineScope.launch {
                subscriptionSheetState.hide()
            }
        }
    }

    fun handleHideSubscriptionSheet() {
        viewModel.onAction(UserAction.ShowPaymentSheet(false))
    }

    fun handleHideAccountSheet() {
        viewModel.onAction(UserAction.ShowAccountSheet(false))
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
                    Napier.e("Sohan Microphone permission denied. Please enable it in app settings.")
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
            UiEvent.NavigateToAddAccount -> navController.navigate(AccountsNavRoutes.AddAccountScreen.route)
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
        if (state.showSubscriptionSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    handleHideSubscriptionSheet()
                },
                sheetState = subscriptionSheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxSize()
            ) {
                RcPaywall(
                    onUpdatePlan = { customerInfo ->
                        subscriptionVM.onAction(
                            PurchaseUserAction.UpdateCurrentPlan(
                                state.uid,
                                customerInfo,
                                true
                            )
                        )
                    },
                    onDismiss = {
                        handleHideSubscriptionSheet()
                    }
                )
            }
        }

        if (state.showAccountSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    handleHideAccountSheet()
                },
                sheetState = accountSheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.wrapContentSize().padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (state.sharedAccounts.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceBetween,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AccountSelectorRow(
                                accounts = state.sharedAccounts,
                                selectedAccountId = state.selectedAccountId,
                                onAccountSelected = { account ->
                                    viewModel.onAction(
                                        UserAction.SelectAccount(account.id, account.accountName)
                                    )
                                }
                            )

                            AppButton(
                                text = "Update ${state.selectedAccountName}",
                                onClick = {
                                    viewModel.onAction(UserAction.SaveIntoSharedAccounts(state.msgIdForAccount))
                                }
                            )
                        }

                    } else {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier.size(80.dp).background(
                                    color = MaterialTheme.colorScheme.primary, shape = CircleShape
                                ), contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.GroupAdd,
                                    contentDescription = "Group Icon",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(40.dp)
                                )

                            }

                            Spacer(Modifier.height(24.dp))

                            Text(
                                text = "No Accounts Found",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Please create a shared account to continue",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            AppButton(
                                text = "Create Shared Account",
                                onClick = {
                                    viewModel.onAction(UserAction.NavigateToAddAccount)
                                }
                            )

                        }
                    }
                }

            }
        }
    }
}

