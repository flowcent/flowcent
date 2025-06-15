package com.aiapp.flowcent.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aiapp.flowcent.chat.presentation.ChatUtil.buildExpensePrompt
import com.aiapp.flowcent.chat.presentation.ChatUtil.checkInvalidExpense
import com.aiapp.flowcent.chat.presentation.ChatUtil.cleanJsonFromMarkdown
import com.aiapp.flowcent.data.common.ChatMessage
import com.aiapp.flowcent.data.common.ChatResult
import dev.shreyaspatil.ai.client.generativeai.GenerativeModel
import dev.shreyaspatil.ai.client.generativeai.type.content
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


class ChatViewModel : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.SendMessage -> {
                sendMessage(action.text)
            }

            UserAction.CancelRecording -> {}
            UserAction.StartRecording -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.CheckAudioPermission)
                }
            }

            UserAction.StopRecording -> {
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
        }
    }


    private val generativeModel = GenerativeModel(
        modelName = "gemma-3-27b-it", apiKey = "AIzaSyDgyl1Ir3VxoXid_cdP57EF67-troYy7oI"
    )


    private suspend fun sendPrompt(prompt: String) {
        _chatState.update { it.copy(isCircularLoading = true) }

        try {
            val chatResult = if (checkInvalidExpense(prompt).isEmpty()) {
                val updatedPrompt = buildExpensePrompt(prompt)
                val response = generativeModel.generateContent(content { text(updatedPrompt) })
                val rawJson = response.text ?: throw IllegalStateException("No response text")
                val cleanJson = cleanJsonFromMarkdown(rawJson)
                Json.decodeFromString<ChatResult>(cleanJson)
            } else {
                val cleanJson = checkInvalidExpense(prompt)
                Json.decodeFromString<ChatResult>(cleanJson)
            }

            _chatState.update {
                it.copy(
                    isCircularLoading = false,
                    messages = it.messages + ChatMessage(
                        chatResult.answer, false, expenseItems = chatResult.data
                    ),
                )
            }

        } catch (e: Exception) {
            println("Error in sendPrompt: ${e.message}")
            _chatState.update {
                it.copy(
                    isCircularLoading = false,
                    answer = "Sorry I could not understand your message",
                    error = e.message
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