package com.aiapp.flowcent.userOnboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.accounts.data.repository.AccountRepository
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.chat.domain.model.ChatHistory
import com.aiapp.flowcent.chat.domain.model.ChatMessage
import com.aiapp.flowcent.chat.domain.model.ChatResult
import com.aiapp.flowcent.chat.domain.utils.ChatUtil.buildExpensePrompt
import com.aiapp.flowcent.chat.domain.utils.ChatUtil.checkInvalidExpense
import com.aiapp.flowcent.core.data.model.TransactionDto
import com.aiapp.flowcent.core.data.repository.ExpenseRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.domain.utils.Resource
import com.aiapp.flowcent.core.domain.utils.toExpenseItemDto
import com.aiapp.flowcent.core.presentation.platform.FlowCentAi
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentTimeInMilli
import com.aiapp.flowcent.core.utils.getTransactionId
import com.aiapp.flowcent.userOnboarding.navigation.UserObNavRoutes
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserObViewModel(
    private val flowCentAi: FlowCentAi,
    private val expenseRepository: ExpenseRepository,
    private val prefRepository: PrefRepository,
    private val authRepository: AuthRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {
    private val _state = MutableStateFlow(UserOnboardingState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.NavigateToUserOnboard -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(UserObNavRoutes.UserOnboardingScreen.route))
                }
            }

            UserAction.NavigateToChatOnboard -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(UserObNavRoutes.ChatOnboardScreen.route))
                }
            }

            is UserAction.UpdateText -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            userText = action.text
                        )
                    }
                }
            }

            is UserAction.SendMessage -> {
                sendMessage(action.text)
            }

            is UserAction.ResetAllState -> {
                _state.update {
                    it.copy(
                        isLoading = false,
                        histories = emptyList(),
                        isSendingMessage = false,
                        userText = "",
                        uid = "",
                        expenseItems = emptyList(),
                        isListening = false,
                        showSaveButton = false
                    )
                }
            }
        }
    }


    private fun sendMessage(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userMessage = ChatMessage.ChatUserMessage(
                id = getUuid(), text = text
            )

            val botLoadingMessageId = getUuid()
            val botLoadingMessage = ChatMessage.ChatBotMessage(
                id = botLoadingMessageId, text = "Thinking...", isLoading = true
            )

            _state.update { currentState ->
                val updatedHistories = if (currentState.histories.isEmpty()) {
                    // No history exists yet, create a new one
                    listOf(
                        ChatHistory(
                            id = getUuid(), messages = listOf(userMessage, botLoadingMessage)
                        )
                    )
                } else {
                    // Append to the last history section
                    val lastHistory = currentState.histories.last()
                    val newHistory = lastHistory.copy(
                        messages = lastHistory.messages + userMessage + botLoadingMessage
                    )
                    currentState.histories.dropLast(1) + newHistory
                }

                currentState.copy(
                    histories = updatedHistories,
                    isSendingMessage = true,
                    userText = "",
                )
            }

            sendPrompt(text, botLoadingMessageId)
        }
    }


    private suspend fun sendPrompt(prompt: String, botLoadingMessageId: String) {
        try {
            val hasInvalidPrompt = checkInvalidExpense(prompt)

            if (hasInvalidPrompt.isEmpty()) {
                handleValidPrompt(prompt, botLoadingMessageId)
            } else {
                handleInvalidPrompt(hasInvalidPrompt, botLoadingMessageId)
            }
        } catch (e: Exception) {
            updateChatStateWithError(botLoadingMessageId, e.message)
        }
    }

    private suspend fun handleValidPrompt(prompt: String, botLoadingMessageId: String) {
        val updatedPrompt = buildExpensePrompt(prompt)
        val result = flowCentAi.generateContent(updatedPrompt)

        result.onSuccess { chatResult ->
            updateChatStateWithResult(botLoadingMessageId, chatResult)
        }.onFailure { error ->
            updateChatStateWithError(botLoadingMessageId, error.message)
        }
    }

    private suspend fun handleInvalidPrompt(invalidPrompt: String, botLoadingMessageId: String) {
        delay(5000)
        val chatResult = Json.decodeFromString<ChatResult>(invalidPrompt)
        updateChatStateWithResult(botLoadingMessageId, chatResult)
    }

    private fun updateChatStateWithResult(messageId: String, chatResult: ChatResult) {
        _state.update { currentState ->
            val updatedHistories = currentState.histories.map { history ->
                val updatedMessages = history.messages.map { msg ->
                    if (msg.id == messageId && msg is ChatMessage.ChatBotMessage) {
                        msg.copy(
                            text = chatResult.answer,
                            isLoading = false,
                            expenseItems = chatResult.data
                        )
                    } else msg
                }
                history.copy(messages = updatedMessages)
            }

            currentState.copy(
                histories = updatedHistories,
                isSendingMessage = false,
                expenseItems = chatResult.data,
                showSaveButton = chatResult.data.isNotEmpty()
            )
        }
    }


    private fun updateChatStateWithError(messageId: String, errorMessage: String?) {
        _state.update { currentState ->
            val updatedHistories = currentState.histories.map { history ->
                val updatedMessages = history.messages.map { msg ->
                    if (msg.id == messageId && msg is ChatMessage.ChatBotMessage) {
                        msg.copy(
                            text = errorMessage ?: "Something unexpected happened",
                            isLoading = false,
                            expenseItems = emptyList()
                        )
                    } else msg
                }
                history.copy(messages = updatedMessages)
            }

            currentState.copy(
                histories = updatedHistories, isSendingMessage = false
            )
        }
    }


    private suspend fun saveIntoPersonal() {
        when (val result = expenseRepository.saveExpenseItemsToDb(
            _state.value.uid, createTransactionPayload(_state.value.expenseItems)
        )) {
            is Resource.Success -> {
                Napier.e("Sohan Expense saved successfully! ${result.data}")
            }

            is Resource.Error -> {
                Napier.e("Sohan Error saving expense: ${result.message}")
            }

            Resource.Loading -> {
                // Optionally handle loading state in UI
            }
        }
    }

    private fun createTransactionPayload(expenseItems: List<ExpenseItem>): TransactionDto {
        return TransactionDto(
            totalAmount = expenseItems.sumOf { it.amount },
            category = expenseItems.firstOrNull()?.category ?: "",
            createdAt = getCurrentTimeInMilli(),
            createdBy = _state.value.uid,
            transactionId = getTransactionId(),
            updatedAt = getCurrentTimeInMilli(),
            updatedBy = _state.value.uid,
            uid = _state.value.uid,
            expenses = expenseItems.map { it.toExpenseItemDto() })
    }


    @OptIn(ExperimentalUuidApi::class)
    private fun getUuid(): String {
        return Uuid.random().toString()
    }

}