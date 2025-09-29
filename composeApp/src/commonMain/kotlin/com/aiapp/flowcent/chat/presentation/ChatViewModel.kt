package com.aiapp.flowcent.chat.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.accounts.data.repository.AccountRepository
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.chat.domain.model.AccountSelectionType
import com.aiapp.flowcent.chat.domain.model.ChatHistory
import com.aiapp.flowcent.chat.domain.model.ChatMessage
import com.aiapp.flowcent.chat.domain.model.ChatResult
import com.aiapp.flowcent.chat.domain.utils.ChatUtil.buildExpensePrompt
import com.aiapp.flowcent.chat.domain.utils.ChatUtil.checkInvalidExpense
import com.aiapp.flowcent.core.utils.getTransactionId
import com.aiapp.flowcent.core.data.model.TransactionDto
import com.aiapp.flowcent.core.data.repository.ExpenseRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.domain.utils.EnumConstants
import com.aiapp.flowcent.core.domain.utils.Resource
import com.aiapp.flowcent.core.domain.utils.toExpenseItemDto
import com.aiapp.flowcent.core.presentation.platform.FlowCentAi
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentTimeInMilli
import com.aiapp.flowcent.subscription.data.FeatureLimits
import com.aiapp.flowcent.subscription.util.SubscriptionUtil
import io.github.aakira.napier.Napier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.math.abs
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ChatViewModel(
    private val flowCentAi: FlowCentAi,
    private val expenseRepository: ExpenseRepository,
    private val prefRepository: PrefRepository,
    private val accountRepository: AccountRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _chatState = MutableStateFlow(ChatState())
    val chatState: StateFlow<ChatState> = _chatState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.SendMessage -> {
                val hasEnoughChatCredits = _chatState.value.remainingAiChatCredits > 0
                if (hasEnoughChatCredits) {
                    sendMessage(action.text)
                } else {
                    _chatState.update {
                        it.copy(
                            showSubscriptionSheet = true
                        )
                    }
                }
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
                            originalVoiceText = action.textFromSpeech
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
                viewModelScope.launch {
                    updateMessageDiscardState(action.msgId, true)

                    delay(500)

                    updateMessageDiscardState(
                        msgId = action.msgId,
                        isLoading = false,
                        hasDiscarded = true
                    )
                }
            }

            is UserAction.SaveExpenseItemsToDb -> {
                viewModelScope.launch {
                    val hasEnoughCredits = _chatState.value.remainingCredits > 0
                    if (hasEnoughCredits) {
                        if (_chatState.value.selectionType == AccountSelectionType.SHARED) {
                            _chatState.update {
                                it.copy(
                                    showAccountSheet = true,
                                    msgIdForAccount = action.msgId
                                )
                            }
                        } else {
                            saveIntoPersonal(action.msgId)
                        }
                    } else {
                        _chatState.update {
                            it.copy(
                                showSubscriptionSheet = true
                            )
                        }
                    }
                }
            }

            UserAction.FetchUserUId -> {
                viewModelScope.launch {
                    if (_chatState.value.uid.isNotEmpty()) {
                        fetchSharedAccounts(_chatState.value.uid)
                        fetchUserProfile(_chatState.value.uid)
                    } else {
                        prefRepository.uid.collect { uidFromDataStore ->
                            _chatState.update { currentState ->
                                currentState.copy(uid = uidFromDataStore ?: "")
                            }
                            fetchSharedAccounts(uidFromDataStore)
                            fetchUserProfile(uidFromDataStore)
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

            is UserAction.UpdateListening -> {
                viewModelScope.launch {
                    _chatState.update { currentState ->
                        currentState.copy(
                            isListening = action.isListening
                        )
                    }
                }
            }

            is UserAction.NavigateToChatScreen -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateToChat)
                }
            }

            UserAction.NavigateToBack -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateToBack)
                }
            }

            is UserAction.DeleteExpenseItem -> {
                viewModelScope.launch {
                    _chatState.update { currentState ->
                        val updatedHistories = currentState.histories.map { history ->
                            val updatedMessages = history.messages.map { message ->
                                if (message.id == action.messageId && message is ChatMessage.ChatBotMessage) {
                                    val updatedItems = message.expenseItems.toMutableList()
                                    updatedItems.remove(action.expenseItem)
                                    message.copy(expenseItems = updatedItems)
                                } else message
                            }
                            history.copy(messages = updatedMessages)
                        }
                        currentState.copy(histories = updatedHistories)
                    }
                }
            }


            is UserAction.EditExpenseItem -> {
                viewModelScope.launch {}
            }

            is UserAction.ShowPaymentSheet -> {
                viewModelScope.launch {
                    _chatState.update { currentState ->
                        currentState.copy(
                            showSubscriptionSheet = action.sheetState
                        )
                    }
                }
            }

            is UserAction.ShowAccountSheet -> {
                viewModelScope.launch {
                    _chatState.update { currentState ->
                        currentState.copy(
                            showAccountSheet = action.sheetState
                        )
                    }
                }
            }

            UserAction.NavigateToAddAccount -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateToAddAccount)
                    _chatState.update { currentState ->
                        currentState.copy(
                            showAccountSheet = false
                        )
                    }
                }
            }

            is UserAction.SaveIntoSharedAccounts -> {
                viewModelScope.launch {
                    saveIntoSharedAccounts(action.msgId)
                }
            }
        }
    }

    private fun updateMessageDiscardState(
        msgId: String,
        isLoading: Boolean,
        hasDiscarded: Boolean = false
    ) {
        _chatState.update { currentState ->
            val updatedHistories = currentState.histories.map { history ->
                val updatedMessages = history.messages.map { msg ->
                    if (msg.id == msgId && msg is ChatMessage.ChatBotMessage) {
                        msg.copy(
                            isLoadingDiscard = isLoading,
                            hasDiscarded = hasDiscarded
                        )
                    } else msg
                }
                history.copy(messages = updatedMessages)
            }
            currentState.copy(histories = updatedHistories)
        }
    }

    private fun fetchUserProfile(uid: String?) {
        if (uid.isNullOrEmpty()) {
            Napier.e("Sohan 404 No User Found")
            return
        }
        viewModelScope.launch {
            when (val result = authRepository.fetchUserProfile(uid)) {
                is Resource.Error -> {
                    Napier.e("Sohan Error in fetching user profile: ${result.message}")
                }

                Resource.Loading -> {}
                is Resource.Success -> {
                    val user = result.data ?: return@launch
                    val features = SubscriptionUtil
                        .getSubscriptionFeatures(
                            SubscriptionUtil.getSubscriptionPlan(user.subscription.currentEntitlementId)
                        )

                    val (remainingCredits, remainingAiChatCredits) = calculateRemainingCredits(
                        user.totalRecordCredits,
                        user.totalAiChatCredits,
                        features
                    )

                    _chatState.update {
                        it.copy(
                            user = user,
                            remainingCredits = remainingCredits,
                            remainingAiChatCredits = remainingAiChatCredits
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

    private suspend fun saveIntoSharedAccounts(msgId: String) {
        when (val result = accountRepository.addAccountTransaction(
            chatState.value.selectedAccountId,
            createTransactionPayload(chatState.value.checkedExpenseItems)
        )) {
            is Resource.Error -> {
                Napier.e("Sohan Error in adding account transaction: ${result.message}")
            }

            Resource.Loading -> {
                updateMessageSaveState(msgId = msgId, isLoading = true)
            }

            is Resource.Success -> {
                Napier.e("Sohan Success in adding account transaction: ${result.data}")
                updateMessageSaveState(msgId = msgId, isLoading = false, hasSaved = true)
            }
        }
    }

    private suspend fun saveIntoPersonal(msgId: String) {
        when (val result = expenseRepository.saveExpenseItemsToDb(
            _chatState.value.uid, createTransactionPayload(chatState.value.checkedExpenseItems)
        )) {
            is Resource.Success -> {
                Napier.e("Sohan Expense saved successfully! ${result.data}")
                updateUserTotalRecords()
                updateMessageSaveState(msgId = msgId, isLoading = false, hasSaved = true)
            }

            is Resource.Error -> {
                Napier.e("Sohan Error saving expense: ${result.message}")
            }

            Resource.Loading -> {
                updateMessageSaveState(msgId = msgId, isLoading = true)
            }
        }
    }

    private fun updateMessageSaveState(
        msgId: String,
        isLoading: Boolean,
        hasSaved: Boolean = false
    ) {
        _chatState.update { currentState ->
            val updatedHistories = currentState.histories.map { history ->
                val updatedMessages = history.messages.map { msg ->
                    if (msg.id == msgId && msg is ChatMessage.ChatBotMessage) {
                        msg.copy(
                            isLoadingSave = isLoading,
                            hasSaved = hasSaved
                        )
                    } else msg
                }
                history.copy(messages = updatedMessages)
            }
            currentState.copy(histories = updatedHistories)
        }
    }

    private fun updateUserTotalRecords() {
        viewModelScope.launch {
            val credits = (_chatState.value.user?.totalRecordCredits ?: 0) + 1
            when (val result =
                authRepository.updateTotalRecordCredits(_chatState.value.uid, credits)) {
                is Resource.Error -> {
                    Napier.e("Sohan Error in updating user totalRecordCredits: ${result.message}")
                }

                Resource.Loading -> {}
                is Resource.Success -> {
                    Napier.e("Sohan Success in updating user totalRecordCredits: ${result.data}")
                    _chatState.update { currentState ->
                        currentState.copy(
                            user = currentState.user?.copy(totalRecordCredits = credits)
                        )
                    }
                }
            }
        }
    }

    private fun createTransactionPayload(expenseItems: List<ExpenseItem>): TransactionDto {
        return TransactionDto(
            totalAmount = expenseItems.sumOf { it.amount },
            totalExpenseAmount = expenseItems
                .filter { it.type == EnumConstants.TransactionType.EXPENSE }
                .sumOf { it.amount },
            totalIncomeAmount = expenseItems
                .filter { it.type == EnumConstants.TransactionType.INCOME }
                .sumOf { it.amount },
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
            updateUserTotalAiChatRecords()
        }.onFailure { error ->
            updateChatStateWithError(botLoadingMessageId, error.message)
        }
    }

    private fun updateUserTotalAiChatRecords() {
        viewModelScope.launch {
            val credits = (_chatState.value.user?.totalAiChatCredits ?: 0) + 1

            when (val result =
                authRepository.updateTotalChatCredits(_chatState.value.uid, credits)) {
                is Resource.Error -> {
                    Napier.e("Sohan Error in updating user totalAiChatCredits: ${result.message}")
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    Napier.e("Sohan Success in updating user totalAiChatCredits: ${result.data}")
                    _chatState.update { currentState ->
                        currentState.copy(
                            user = currentState.user?.copy(totalAiChatCredits = credits)
                        )
                    }
                }
            }
        }
    }

    private suspend fun handleInvalidPrompt(invalidPrompt: String, botLoadingMessageId: String) {
        delay(5000) // Simulate response delay for invalid prompt
        val chatResult = Json.decodeFromString<ChatResult>(invalidPrompt)
        updateChatStateWithResult(botLoadingMessageId, chatResult)
    }

    private fun updateChatStateWithResult(messageId: String, chatResult: ChatResult) {
        _chatState.update { currentState ->
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
                isSendingMessage = false
            )
        }
    }


    private fun updateChatStateWithError(messageId: String, errorMessage: String?) {
        _chatState.update { currentState ->
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
                histories = updatedHistories,
                isSendingMessage = false
            )
        }
    }


    private fun sendMessage(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val userMessage = ChatMessage.ChatUserMessage(
                id = getUuid(),
                text = text
            )

            val botLoadingMessageId = getUuid()
            val botLoadingMessage = ChatMessage.ChatBotMessage(
                id = botLoadingMessageId,
                text = "Thinking...",
                isLoading = true
            )

            _chatState.update { currentState ->
                val updatedHistories = if (currentState.histories.isEmpty()) {
                    // No history exists yet, create a new one
                    listOf(
                        ChatHistory(
                            id = getUuid(),
                            messages = listOf(userMessage, botLoadingMessage)
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
                    showAccounts = false
                )
            }

            sendPrompt(text, botLoadingMessageId)
        }
    }

    private fun calculateRemainingCredits(
        usedTransactions: Int,
        usedAiChats: Int,
        features: FeatureLimits?
    ): Pair<Int, Int> {
        val remainingTransactions = features?.maxTransactionsPerMonth
            ?.let { it - usedTransactions }
            ?.coerceAtLeast(0) ?: 0

        val remainingAiChats = features?.maxAiChatsPerMonth
            ?.let { it - usedAiChats }
            ?.coerceAtLeast(0) ?: 0

        return remainingTransactions to remainingAiChats
    }


    @OptIn(ExperimentalUuidApi::class)
    private fun getUuid(): String {
        return Uuid.random().toString()
    }

}