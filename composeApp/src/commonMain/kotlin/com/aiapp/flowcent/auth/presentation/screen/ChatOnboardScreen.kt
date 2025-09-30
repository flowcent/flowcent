package com.aiapp.flowcent.auth.presentation.screen

import ChatOnBoardTypingText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.ChatOnBoardInput
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.ChatOnboardItem
import com.aiapp.flowcent.core.presentation.components.AppButton

@Composable
fun ChatOnboardScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    state: AuthState
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.histories) {
        if (state.histories.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onAction(UserAction.ResetChatState)
        }
    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        bottomBar = {
            if (state.showSaveButton) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppButton(
                        isLoading = state.saveButtonLoading,
                        onClick = {
                            viewModel.onAction(UserAction.CreateNewUser)
                        },
                        height = 60.dp,
                        text = "Save it",
                        textStyle = TextStyle(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            fontWeight = FontWeight.SemiBold
                        ),
                        hasGradient = true,
                        textColor = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ChatOnBoardInput(
                        state = state,
                        isListening = state.isListening,
                        onUpdateText = { text ->
                            viewModel.onAction(UserAction.UpdateText(text))
                        },
                        onClickMic = {

                        },
                        onClickSend = {
                            viewModel.onAction(UserAction.SendMessage(state.userText))
                        }
                    )
                }
            }
        }
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                reverseLayout = state.histories.isNotEmpty(),
                contentPadding = PaddingValues(
                    top = 50.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            ) {
                if (state.histories.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 100.dp),
                            contentAlignment = Alignment.Center
                        ) {

                            ChatOnBoardTypingText(
                                onClickText = { text ->
                                    viewModel.onAction(UserAction.SendMessage(text))
                                }
                            )
                        }
                    }
                }


                state.histories.forEach { history ->
                    itemsIndexed(
                        history.messages.reversed(),
                        key = { _, message -> message.id }) { index, message ->
                        // First item in the reversed list is the newest/topmost prompt
                        val itemModifier = if (index == 0) {
                            Modifier
                                .fillParentMaxHeight() // Fill the screen for the prompt
                                .fillMaxWidth()
                        } else {
                            Modifier.fillMaxWidth()
                        }

                        ChatOnboardItem(
                            message = message,
                            state = state,
                            modifier = itemModifier
                        )
                    }
                }
            }
        }
    }


}