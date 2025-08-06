package com.aiapp.flowcent.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.chat.domain.model.ChatMessage
import com.aiapp.flowcent.chat.domain.model.ChatResult
import com.aiapp.flowcent.chat.domain.utils.ChatUtil.buildExpensePrompt
import com.aiapp.flowcent.chat.domain.utils.ChatUtil.checkInvalidExpense
import com.aiapp.flowcent.core.data.model.TransactionDto
import com.aiapp.flowcent.core.data.repository.ExpenseRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.presentation.platform.FlowCentAi
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentTimeInMilli
import com.aiapp.flowcent.core.domain.utils.Resource
import com.aiapp.flowcent.chat.domain.utils.getTransactionId
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
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatViewModel(
    private val flowCentAi: FlowCentAi,
    private val expenseRepository: ExpenseRepository,
    private val prefRepository: PrefRepository
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

            is UserAction.DiscardExpenseItems -> {

            }

            is UserAction.SaveExpenseItemsToDb -> {
                viewModelScope.launch {
                    when (val result =
                        expenseRepository.saveExpenseItemsToDb(
                            _chatState.value.uid,
                            createTransactionPayload(action.expenseItems)
                        )) {
                        is Resource.Success -> {
                            println("Sohan Expense saved successfully! ${result.data}")
                        }

                        is Resource.Error -> {
                            println("Sohan Error saving expense: ${result.message}")
                        }

                        Resource.Loading -> {
                            // Optionally handle loading state in UI
                        }
                    }

                }
            }

            UserAction.FetchUserUId -> {
                viewModelScope.launch {
                    prefRepository.uid.collect { uidFromDataStore ->
                        _chatState.update { currentState ->
                            currentState.copy(uid = uidFromDataStore ?: "")
                        }
                    }
                }
            }
        }
    }

    private fun createTransactionPayload(expenseItems: List<ExpenseItem>): TransactionDto {
        return TransactionDto(
            totalAmount = expenseItems.sumOf { it.amount },
            category = expenseItems.firstOrNull()?.category ?: "",
            createdAt = getCurrentTimeInMilli(),
            createdBy = _chatState.value.uid,
            transactionId = getTransactionId(),
            updatedAt = getCurrentTimeInMilli(),
            updatedBy = _chatState.value.uid,
            uid = _chatState.value.uid,
            expenses = expenseItems
        )
    }

    private suspend fun sendPrompt(prompt: String, botLoadingMessageId: String) {
        try {
            val hasInvalidPrompt = checkInvalidExpense(prompt)

            if (hasInvalidPrompt.isEmpty()) {
                val updatedPrompt = buildExpensePrompt(prompt)
                val result = flowCentAi.generateContent(updatedPrompt)
                result.onSuccess { chatResult ->
                    println("Sohan sendPrompt chatResult: ${chatResult.data}")
//                    _chatState.update {
//                        it.copy(
//                            messages = it.messages + ChatMessage(
//                                chatResult.answer,
//                                false,
//                                expenseItems = chatResult.data,
//                                isLoading = false
//                            ),
//                        )
//                    }
                    _chatState.update { currentState ->
                        val updatedMessages = currentState.messages.map { msg ->
                            if (msg.id == botLoadingMessageId) {
                                msg.copy(
                                    text = chatResult.answer,
                                    isLoading = false, // Set loading to false
                                    expenseItems = chatResult.data
                                )
                            } else {
                                msg
                            }
                        }
                        currentState.copy(
                            messages = updatedMessages,
                            isSendingMessage = false // Release the input field lock
                        )
                    }

                }
                    .onFailure { error ->
//                        _chatState.update { currentState ->
//                            currentState.copy(
//                                error = "Error: ${error.message ?: "Unknown AI error"}",
//                                messages = currentState.messages + ChatMessage(
//                                    error.message ?: "Something unexpected happened",
//                                    false,
//                                    expenseItems = emptyList(),
//                                    isLoading = false
//                                ),
//                            )
//                        }

                        _chatState.update { currentState ->
                            val updatedMessages = currentState.messages.map { msg ->
                                if (msg.id == botLoadingMessageId) {
                                    msg.copy(
                                        text = error.message ?: "Something unexpected happened",
                                        isLoading = false,
                                        expenseItems = emptyList() // Clear any partial data on error
                                    )
                                } else {
                                    msg
                                }
                            }
                            currentState.copy(
                                messages = updatedMessages,
                                isSendingMessage = false // Release the input field lock even on error
                            )
                        }
                    }

            } else {
                val chatResult = Json.decodeFromString<ChatResult>(hasInvalidPrompt)
                _chatState.update { currentState ->
                    val updatedMessages = currentState.messages.map { msg ->
                        if (msg.id == botLoadingMessageId) {
                            msg.copy(
                                text = chatResult.answer,
                                isLoading = false, // Set loading to false
                                expenseItems = chatResult.data
                            )
                        } else {
                            msg
                        }
                    }
                    currentState.copy(
                        messages = updatedMessages,
                        isSendingMessage = false // Release the input field lock
                    )
                }
            }
        } catch (e: Exception) {
            _chatState.update { currentState ->
                val updatedMessages = currentState.messages.map { msg ->
                    if (msg.id == botLoadingMessageId) {
                        msg.copy(
                            text = e.message ?: "Something unexpected happened",
                            isLoading = false,
                            expenseItems = emptyList()
                        )
                    } else {
                        msg
                    }
                }
                currentState.copy(
                    messages = updatedMessages,
                    isSendingMessage = false
                )
            }
        }
    }

    private fun sendMessage(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userMessage = ChatMessage(text = text, isUser = true)
            val botLoadingMessageId = getUuid()

            val botLoadingMessage = ChatMessage(
                id = botLoadingMessageId,
                text = "Thinking...",
                isUser = false,
                isLoading = true
            )

            _chatState.update { currentState ->
                currentState.copy(
                    messages = currentState.messages + userMessage + botLoadingMessage,
                    isSendingMessage = true,
                    userText = ""
                )
            }
            sendPrompt(text, botLoadingMessageId)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun getUuid(): String {
        return Uuid.random().toString()
    }

}