package com.aiapp.flowcent.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.auth.presentation.navigation.AuthNavRoutes
import com.aiapp.flowcent.chat.domain.model.ChatHistory
import com.aiapp.flowcent.chat.domain.model.ChatMessage
import com.aiapp.flowcent.chat.domain.model.ChatResult
import com.aiapp.flowcent.chat.domain.utils.ChatUtil.buildExpensePrompt
import com.aiapp.flowcent.chat.domain.utils.ChatUtil.checkInvalidExpense
import com.aiapp.flowcent.core.data.model.TransactionDto
import com.aiapp.flowcent.core.data.repository.ExpenseRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.domain.utils.Constants
import com.aiapp.flowcent.core.domain.utils.EnumConstants
import com.aiapp.flowcent.core.domain.utils.Resource
import com.aiapp.flowcent.core.domain.utils.toExpenseItemDto
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.platform.FlowCentAi
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentTimeInMilli
import com.aiapp.flowcent.core.utils.DialogType
import com.aiapp.flowcent.core.utils.getFlowCentUserId
import com.aiapp.flowcent.core.utils.getTransactionId
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
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
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val prefRepository: PrefRepository,
    private val expenseRepository: ExpenseRepository,
    private val flowCentAi: FlowCentAi
) : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onAction(action: UserAction) {
        when (action) {
            UserAction.FirebaseSignOut -> {
                viewModelScope.launch {
                    if (Firebase.auth.currentUser != null) {
                        Firebase.auth.signOut()
                    }
                    prefRepository.deleteUid()
                    _uiEvent.send(UiEvent.Navigate(AuthNavRoutes.SignInScreen.route))
                }
            }

            is UserAction.CreateNewUser -> {
                createNewUserToDb(
                    User(
                        uid = _state.value.firebaseUser?.uid ?: "",
                        fullName = _state.value.firebaseUser?.displayName ?: "",
                        email = _state.value.firebaseUser?.email ?: "",
                        phoneNumber = _state.value.countryDetails?.countryPhoneNumberCode + _state.value.phoneNumber,
                        imageUrl = _state.value.firebaseUser?.photoURL ?: "",
                        initialBalance = _state.value.initialBalance,
                        providerId = _state.value.firebaseUser?.providerId ?: "",
                        signInType = _state.value.signInType,
                        createdBy = _state.value.firebaseUser?.uid ?: "",
                        createdAt = getCurrentTimeInMilli(),
                        updatedAt = getCurrentTimeInMilli(),
                        updatedBy = _state.value.firebaseUser?.uid ?: "",
                        localUserName = _state.value.username,
                        savingTarget = _state.value.savingTarget,
                        flowCentUserId = getFlowCentUserId(_state.value.phoneNumber)
                    )
                )

            }

            UserAction.IsLoggedIn -> {}
            is UserAction.IsUserExist -> {
                viewModelScope.launch {
                    when (val result = authRepository.isUserExist(action.firebaseUser.uid)) {
                        is Resource.Error -> {
                            setLoaderOff(action.signInType)
                        }

                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            setLoaderOff(action.signInType)
                            _state.update {
                                it.copy(
                                    uid = action.firebaseUser.uid,
                                    firebaseUser = action.firebaseUser,
                                    signInType = action.signInType
                                )
                            }
                            if (result.data == true) {
                                setUserSignedIn(true)
                                saveUserUid(action.firebaseUser.uid)
                                _uiEvent.send(UiEvent.Navigate(AppNavRoutes.Home.route))
                            } else {
                                _uiEvent.send(UiEvent.Navigate(AuthNavRoutes.UserWelcomeScreen.route))
                            }
                        }
                    }
                }
            }

            is UserAction.SaveUserUid -> saveUserUid(action.uid)
            is UserAction.UpdateInitialBalance -> {
                if (!action.initialBalance.toString().matches(Regex("^\\d*\\.?\\d*\$"))) return
                if (action.initialBalance < 0) return
                if (action.initialBalance > 10000000) return

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            initialBalance = action.initialBalance
                        )
                    }
                }
            }

            is UserAction.UpdatePhoneNumber -> {
                Napier.e("Sohan action.phoneNumber ${action.phoneNumber}")
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            phoneNumber = action.phoneNumber
                        )
                    }
                }
            }

            is UserAction.UpdateCountry -> {
                Napier.e("Sohan action.countryDetails ${action.countryDetails}")
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            countryDetails = action.countryDetails
                        )
                    }
                }
            }

            is UserAction.UpdateUsername -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            username = action.username
                        )
                    }
                }
            }

            UserAction.NavigateToSignIn -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AuthNavRoutes.SignInScreen.route))
                }
            }

            UserAction.NavigateToSignUp -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AuthNavRoutes.SignUpScreen.route))
                }
            }

            is UserAction.UpdateSavingTarget -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            savingTarget = action.amount
                        )
                    }
                }
            }

            is UserAction.OnGoogleSignInResult -> onGoogleSignInResult(action.result)

            UserAction.FetchUserId -> {
                viewModelScope.launch {
                    prefRepository.uid.collect { uidFromDataStore ->
                        _state.update { currentState ->
                            currentState.copy(uid = uidFromDataStore ?: "")
                        }
                        fetchUserProfile(uid = uidFromDataStore)
                    }
                }
            }

            is UserAction.SignInWithEmailPass -> signInWithEmailPass(
                action.email, action.password, Constants.SIGN_IN_TYPE_EMAILPASS
            )

            is UserAction.SignUpWithEMailPass -> {
                signUpWithEmailPass(
                    action.email.trim(),
                    action.password.trim(),
                    action.confirmPassword.trim(),
                    Constants.SIGN_IN_TYPE_EMAILPASS
                )
            }

            is UserAction.ShowPaymentSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showPaymentSheet = action.sheetState
                        )
                    }
                }
            }

            UserAction.NavigateToChatOnboard -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AuthNavRoutes.ChatOnboardScreen.route))
                }
            }

            UserAction.NavigateToUserOnboard -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AuthNavRoutes.UserOnboardingScreen.route))
                }
            }

            UserAction.ResetChatState -> {
                _state.update {
                    it.copy(
                        histories = emptyList(),
                        isSendingMessage = false,
                        userText = "",
                        expenseItems = emptyList(),
                        isListening = false,
                        showSaveButton = false
                    )
                }
            }

            is UserAction.SendMessage -> sendMessage(action.text)
            is UserAction.UpdateText -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            userText = action.text
                        )
                    }
                }
            }

            UserAction.NavigateToChatWelcome -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AuthNavRoutes.ChatWelcomeScreen.route))
                }
            }

            is UserAction.UpdateInputErrors -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            inputErrors = action.errors
                        )
                    }
                }
            }

            UserAction.NavigateToHome -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AppNavRoutes.Home.route))
                }
            }

            UserAction.NavigateBack -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateBack)
                }
            }
        }
    }

    private fun saveUserUid(uid: String) {
        viewModelScope.launch {
            prefRepository.saveUid(uid)
        }
    }

    private fun setUserSignedIn(hasSignedIn: Boolean) {
        _state.update {
            it.copy(
                hasUserSignedIn = hasSignedIn
            )

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
                    Napier.e("Sohan Success in fetching user profile: ${result.data?.localUserName}")
                    _state.update {
                        it.copy(
                            user = result.data
                        )
                    }
                }
            }

        }
    }

    private fun setLoaderOn(signInType: String) {
        _state.update {
            it.copy(
                isEmailSignInProcessing = when (signInType) {
                    Constants.SIGN_IN_TYPE_EMAILPASS -> true
                    else -> it.isEmailSignInProcessing
                },
                isGoogleSignInProcessing = when (signInType) {
                    Constants.SIGN_IN_TYPE_GOOGLE -> true
                    else -> it.isGoogleSignInProcessing
                },
                isAppleSignInProcessing = when (signInType) {
                    Constants.SIGN_IN_TYPE_APPLE -> true
                    else -> it.isAppleSignInProcessing
                },
            )
        }
    }


    private fun setLoaderOff(signInType: String) {
        _state.update {
            it.copy(
                isEmailSignInProcessing = when (signInType) {
                    Constants.SIGN_IN_TYPE_EMAILPASS -> false
                    else -> it.isEmailSignInProcessing
                },
                isGoogleSignInProcessing = when (signInType) {
                    Constants.SIGN_IN_TYPE_GOOGLE -> false
                    else -> it.isGoogleSignInProcessing
                },
                isAppleSignInProcessing = when (signInType) {
                    Constants.SIGN_IN_TYPE_APPLE -> false
                    else -> it.isAppleSignInProcessing
                },
            )
        }
    }


    private fun signInWithEmailPass(email: String, password: String, signInType: String) {
        viewModelScope.launch {
            setLoaderOn(signInType)
            when (val result = authRepository.signInWithEmailAndPassword(email, password)) {
                is Resource.Error -> {
                    Napier.e("Sohan result.message ${result.message}")
                    setLoaderOff(signInType)
                    _uiEvent.send(
                        UiEvent.ShowDialog(
                            dialogType = DialogType.ERROR,
                            title = "Sign In Failed",
                            body = if (result.message.contentEquals(
                                    "The supplied auth credential is incorrect, malformed or has expired.",
                                    ignoreCase = true
                                )
                            ) "The email or password is incorrect, or has expired."
                            else "Sorry! Something went wrong. Please try again later",
                        )
                    )
                }

                Resource.Loading -> {
                    setLoaderOn(signInType)
                }

                is Resource.Success -> {
                    result.data?.let {
                        val firebaseUser = it.user
                        if (firebaseUser != null) {
                            onAction(UserAction.IsUserExist(firebaseUser, signInType))
                        }
                    }
                }
            }
        }
    }


    private fun signUpWithEmailPass(
        email: String, password: String, confirmPassword: String, signInType: String
    ) {
        viewModelScope.launch {
            setLoaderOn(signInType)
            if (password != confirmPassword) {
                setLoaderOff(signInType)
                _uiEvent.send(
                    UiEvent.ShowDialog(
                        dialogType = DialogType.ERROR,
                        title = "Password and Confirm Password does not match",
                    )
                )
            }
            when (val result = authRepository.signUpWithEmailAndPassword(email, password)) {
                is Resource.Error -> {
                    Napier.e("Sohan result.message ${result.message}")
                    setLoaderOff(signInType)
                    _uiEvent.send(
                        UiEvent.ShowDialog(
                            dialogType = DialogType.ERROR,
                            title = "Sign In Failed",
                            body = if (result.message.contentEquals(
                                    "The supplied auth credential is incorrect, malformed or has expired.",
                                    ignoreCase = true
                                )
                            ) "The email or password is incorrect, or has expired."
                            else "Sorry! Something went wrong. Please try again later",
                        )
                    )
                }

                Resource.Loading -> {
                    setLoaderOn(signInType)
                }

                is Resource.Success -> {
                    result.data?.let {
                        val firebaseUser = it.user
                        if (firebaseUser != null) {
                            onAction(UserAction.IsUserExist(firebaseUser, signInType))
                        }
                    }
                }
            }
        }
    }


    private fun onGoogleSignInResult(result: Result<FirebaseUser?>) {
        viewModelScope.launch {
            try {
                if (result.isSuccess) {
                    val firebaseUser = result.getOrNull()
                    if (firebaseUser != null) {
                        onAction(
                            UserAction.IsUserExist(firebaseUser, Constants.SIGN_IN_TYPE_GOOGLE)
                        )
                    }
                } else {
                    if (result.exceptionOrNull() is CancellationException) {
                        Napier.e("Google Sign-In was cancelled by the user.")
                    } else {
                        Napier.e("onFirebaseResult Error: ${result.exceptionOrNull()?.message}")
                        if (result.exceptionOrNull()?.message?.contains("Idtoken is null") == true) {
                            _uiEvent.send(
                                UiEvent.ShowDialog(
                                    dialogType = DialogType.NO_INTERNET,
                                )
                            )
                        }
                    }
                }
            } catch (ex: Exception) {
                Napier.e("onFirebaseResult Exception: ${ex.message}")
            }
        }
    }

    private fun createNewUserToDb(user: User) {
        viewModelScope.launch {
            when (val result = authRepository.createNewUser(user)) {
                is Resource.Loading -> {
                    _state.update {
                        it.copy(
                            saveButtonLoading = true
                        )
                    }
                }

                is Resource.Success -> {
                    Napier.e("Sohan createNewUserToDb success ${result.data}")
                    setUserSignedIn(true)
                    saveUserUid(user.uid)
                    saveExpensesIntoDb()
                    _state.update {
                        it.copy(
                            saveButtonLoading = false
                        )
                    }
                }

                is Resource.Error -> {
                    setUserSignedIn(false)
                    _state.update {
                        it.copy(
                            saveButtonLoading = false
                        )
                    }
                    Napier.e("Sohan createNewUserToDb ${result.message}")
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


    private fun saveExpensesIntoDb() {
        if (_state.value.uid.isEmpty()) {
            Napier.e("Sohan 404 User not fount")
            return
        }
        viewModelScope.launch {
            Napier.e("Sohan _state.value.uid ${_state.value.uid}")
            when (val result = expenseRepository.saveExpenseItemsToDb(
                _state.value.uid, createTransactionPayload(_state.value.expenseItems)
            )) {
                is Resource.Success -> {
                    Napier.e("Sohan Expense saved successfully! ${result.data}")
                    _uiEvent.send(UiEvent.Navigate(AuthNavRoutes.CongratsScreen.route))
                }

                is Resource.Error -> {
                    Napier.e("Sohan Error saving expense: ${result.message}")
                }

                Resource.Loading -> {
                    // Optionally handle loading state in UI
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