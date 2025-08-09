package com.aiapp.flowcent.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.accounts.data.repository.AccountRepository
import com.aiapp.flowcent.chat.domain.model.AccountSelectionType
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
import com.aiapp.flowcent.core.domain.utils.toExpenseItemDto
import io.github.aakira.napier.Napier
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
    private val prefRepository: PrefRepository,
    private val accountRepository: AccountRepository,
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
                    if (_chatState.value.selectionType == AccountSelectionType.SHARED) {
                        saveIntoSharedAccounts()
                    } else {
                        //Personal finance
                        saveIntoPersonal()
                    }

                }
            }

            UserAction.FetchUserUId -> {
                viewModelScope.launch {
                    if (_chatState.value.uid.isNotEmpty()) {
                        fetchSharedAccounts(_chatState.value.uid)
                    } else {
                        prefRepository.uid.collect { uidFromDataStore ->
                            _chatState.update { currentState ->
                                currentState.copy(uid = uidFromDataStore ?: "")
                            }
                            fetchSharedAccounts(uidFromDataStore)
                        }
                    }
                }
            }

            is UserAction.UpdateAccountSelectionType -> {
                _chatState.update { currentState ->
                    currentState.copy(selectionType = action.selectionType)
                }
            }

            is UserAction.SelectAccount -> {
                _chatState.update { currentState ->
                    currentState.copy(
                        selectedAccountId = action.accountDocumentId,
                        selectedAccountName = action.accountName
                    )
                }
            }

            is UserAction.UpdateAllCheckedItems -> {
                viewModelScope.launch {
                    _chatState.update { currentState ->
                        currentState.copy(
                            checkedExpenseItems = action.expenseItems
                        )
                    }
                }
            }

            is UserAction.UpdateCheckedItem -> {
                viewModelScope.launch {
                    _chatState.update { currentState ->
                        val updatedCheckedItems = currentState.checkedExpenseItems.toMutableList()
                        if (action.isChecked) {
                            if (!updatedCheckedItems.contains(action.expenseItem)) {
                                updatedCheckedItems.add(action.expenseItem)
                            }
                        } else {
                            updatedCheckedItems.remove(action.expenseItem)
                        }
                        currentState.copy(
                            checkedExpenseItems = updatedCheckedItems
                        )
                    }
                }
            }
        }
    }

    private fun fetchSharedAccounts(uid: String?) {
        if (uid.isNullOrEmpty()) return
        viewModelScope.launch {
            when (val result = accountRepository.getAccounts(uid)) {
                is Resource.Error -> {
                    _chatState.update { currentState ->
                        currentState.copy(
                            sharedAccounts = emptyList()
                        )
                    }
                }

                Resource.Loading -> {}

                is Resource.Success -> {
                    _chatState.update { currentState ->
                        currentState.copy(
                            sharedAccounts = result.data ?: emptyList()
                        )
                    }
                }
            }
        }
    }

    private suspend fun saveIntoSharedAccounts() {
        when (val result = accountRepository.addAccountTransaction(
            chatState.value.selectedAccountId,
            createTransactionPayload(chatState.value.checkedExpenseItems)
        )) {
            is Resource.Error -> {
                Napier.e("Sohan Error in adding account transaction: ${result.message}")
            }

            Resource.Loading -> {}
            is Resource.Success -> {
                Napier.e("Sohan Success in adding account transaction: ${result.data}")
            }
        }
    }

    private suspend fun saveIntoPersonal() {
        when (val result = expenseRepository.saveExpenseItemsToDb(
            _chatState.value.uid, createTransactionPayload(chatState.value.checkedExpenseItems)
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
            expenses = expenseItems.map { it.toExpenseItemDto() }
        )
    }

    private suspend fun sendPrompt(prompt: String, botLoadingMessageId: String) {
        try {
            val hasInvalidPrompt = checkInvalidExpense(prompt)

            if (hasInvalidPrompt.isEmpty()) {
                val updatedPrompt = buildExpensePrompt(prompt)
                val result = flowCentAi.generateContent(updatedPrompt)
                result.onSuccess { chatResult ->
                    _chatState.update { currentState ->
                        val updatedMessages = currentState.messages.map { msg ->
                            if (msg.id == botLoadingMessageId) {
                                msg.copy(
                                    text = chatResult.answer,
                                    isBotMessageLoading = false, // Set loading to false
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

                }.onFailure { error ->
                    _chatState.update { currentState ->
                        val updatedMessages = currentState.messages.map { msg ->
                            if (msg.id == botLoadingMessageId) {
                                msg.copy(
                                    text = error.message ?: "Something unexpected happened",
                                    isBotMessageLoading = false,
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
                                isBotMessageLoading = false, // Set loading to false
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
                            isBotMessageLoading = false,
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
                isBotMessageLoading = true
            )

            _chatState.update { currentState ->
                currentState.copy(
                    messages = currentState.messages + userMessage + botLoadingMessage,
                    isSendingMessage = true,
                    userText = "",
                    showAccounts = false
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