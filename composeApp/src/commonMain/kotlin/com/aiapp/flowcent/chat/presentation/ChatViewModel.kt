package com.aiapp.flowcent.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.core.platform.FlowCentAi
import com.aiapp.flowcent.chat.presentation.ChatUtil.buildExpensePrompt
import com.aiapp.flowcent.chat.presentation.ChatUtil.checkInvalidExpense
import com.aiapp.flowcent.data.common.ChatMessage
import com.aiapp.flowcent.data.common.ChatResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ChatViewModel(
    private val flowCentAi: FlowCentAi
) : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.SendMessage -> {
                sendMessage(action.text)
            }

            UserAction.StartAudioPlayer -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.StartAudioPlayer)
                }
            }

            UserAction.StopAudioPlayer -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.StopAudioPlayer)
                }
            }

            is UserAction.UpdateText -> {
                viewModelScope.launch {
                    _chatState.update {
                        it.copy(
                            userText = action.text
                        )
                    }
                }
            }

            is UserAction.UpdateVoiceText -> {
                viewModelScope.launch {
                    _chatState.update {
                        it.copy(
                            originalVoiceText = action.originalText,
                            translatedVoiceText = action.translatedText
                        )
                    }
                }
            }

            UserAction.CheckAudioPermission -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.CheckAudioPermission)
                }
            }
        }
    }

    private suspend fun sendPrompt(prompt: String) {
        _chatState.update { it.copy(isCircularLoading = true) }
        try {
            val hasInvalidPrompt = checkInvalidExpense(prompt)

            if (hasInvalidPrompt.isEmpty()) {
                val updatedPrompt = buildExpensePrompt(prompt)
                val result = flowCentAi.generateContent(updatedPrompt)
                result.onSuccess { chatResult ->
                    _chatState.update {
                        it.copy(
                            isCircularLoading = false,
                            messages = it.messages + ChatMessage(
                                chatResult.answer,
                                false,
                                expenseItems = chatResult.data
                            ),
                        )
                    }
                }
                    .onFailure { error ->
                        _chatState.update { currentState ->
                            currentState.copy(
                                isCircularLoading = false,
                                error = "Error: ${error.message ?: "Unknown AI error"}",
                                messages = currentState.messages + ChatMessage(
                                    error.message ?: "Something unexpected happened",
                                    false,
                                    expenseItems = emptyList()
                                ),
                            )
                        }
                    }

            } else {
                val chatResult = Json.decodeFromString<ChatResult>(hasInvalidPrompt)
                _chatState.update { currentState ->
                    currentState.copy(
                        isCircularLoading = false,
                        messages = currentState.messages + ChatMessage(
                            chatResult.answer,
                            false,
                            expenseItems = chatResult.data
                        ),
                    )
                }
            }
        } catch (e: Exception) {
            _chatState.update { currentState ->
                currentState.copy(
                    isCircularLoading = false,
                    error = "Error: ${e.message ?: "Unknown AI error"}",
                    messages = currentState.messages + ChatMessage(
                        e.message ?: "Something unexpected happened",
                        false,
                        expenseItems = emptyList()
                    ),
                )
            }
        }
    }

    private fun sendMessage(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _chatState.update {
                it.copy(
                    messages = it.messages + ChatMessage(
                        text, true, expenseItems = emptyList()
                    ),
                    userText = ""
                )
            }
            sendPrompt(text)
        }
    }

}