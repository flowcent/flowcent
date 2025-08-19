package com.aiapp.flowcent.accounts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.accounts.data.model.AccountDto
import com.aiapp.flowcent.accounts.data.repository.AccountRepository
import com.aiapp.flowcent.accounts.domain.utils.toAcMemberDtos
import com.aiapp.flowcent.accounts.domain.utils.toMemberIds
import com.aiapp.flowcent.accounts.domain.utils.getAccountID
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.core.data.model.TransactionDto
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.presentation.platform.ContactFetcher
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils
import com.aiapp.flowcent.core.domain.utils.Resource
import com.aiapp.flowcent.core.domain.utils.toTransactions
import com.aiapp.flowcent.core.presentation.platform.ConnectivityObserver
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentDate
import com.aiapp.flowcent.core.utils.DialogType
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val accountRepository: AccountRepository,
    private val prefRepository: PrefRepository,
    private val authRepository: AuthRepository,
    private val contactFetcher: ContactFetcher
) : ViewModel() {
    private val _state = MutableStateFlow(AccountState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                selectedDate = getCurrentDate().toString()
            )
        }
    }

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.ClickAdd -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.ClickAdd)
                }
            }

            is UserAction.FetchRegisteredPhoneNumbers -> {
                fetchRegisteredPhoneNumbers()
            }

            is UserAction.UpdateSheetState -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showSheet = action.sheetState
                        )
                    }
                }
            }

            is UserAction.UpdateAcInitialBalance -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            acInitialBalance = action.initialBalance
                        )
                    }
                }
            }

            is UserAction.UpdateAccountName -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            accountName = action.accountName
                        )
                    }
                }
            }

            is UserAction.OnUserCheckedChange -> {
                viewModelScope.launch {
                    val selectedUserSet = _state.value.selectedUsers.toMutableSet()
                    if (action.checked) {
                        selectedUserSet.add(action.user)
                    } else {
                        selectedUserSet.remove(action.user)
                    }
                    _state.update {
                        it.copy(
                            selectedUsers = selectedUserSet.toList()
                        )
                    }
                }
            }

            is UserAction.OnRemoveUser -> {
                viewModelScope.launch {
                    val selectedUserSet =
                        _state.value.selectedUsers.toMutableSet()
                    selectedUserSet.remove(action.user)
                    _state.update {
                        it.copy(
                            selectedUsers = selectedUserSet.toList()
                        )
                    }
                }
            }

            UserAction.CreateAccount -> {
                createAccount()
            }

            UserAction.FetchUserUId -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    if (_state.value.uid.isNotEmpty()) {
                        fetchUserProfile(_state.value.uid)
                        fetchAccounts(_state.value.uid)
                    } else {
                        prefRepository.uid.collect { uidFromDataStore ->
                            _state.update { currentState ->
                                currentState.copy(uid = uidFromDataStore ?: "")
                            }
                            fetchUserProfile(uidFromDataStore)
                            fetchAccounts(uidFromDataStore)
                        }
                    }
                }
            }

            is UserAction.OnAccountItemClick -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            selectedAccount = action.account
                        )
                    }
                    _uiEvent.send(UiEvent.NavigateToAccountDetail)
                }
            }

            is UserAction.AddTransactionToAccount -> {}
            is UserAction.GetDailyTransactions -> {
                getDailyTransactions(_state.value.selectedAccount?.id)
            }

            UserAction.NavigateToChat -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateToChat)
                }
            }

            is UserAction.SetSelectedDate -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            selectedDate = action.dateString.toString()
                        )
                    }
                }
            }

            is UserAction.GetUsersDailyTransaction -> {
                getUsersDailyTransactions(_state.value.selectedAccount?.id, action.uid)
            }

            is UserAction.CheckInternet -> {
                viewModelScope.launch {
                    if (action.status == ConnectivityObserver.Status.Unavailable) {
                        _uiEvent.send(
                            UiEvent.ShowDialog(
                                dialogType = DialogType.NO_INTERNET
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun fetchUserProfile(uid: String?) {
        if (uid == null) return
        when (val result = authRepository.fetchUserProfile(uid)) {
            is Resource.Error -> {
                println("Sohan Error in fetching user profile: ${result.message}")
            }

            Resource.Loading -> {}
            is Resource.Success -> {
                _state.update {
                    it.copy(
                        currentUserProfile = result.data
                    )
                }
            }
        }
    }

    private fun getUsersDailyTransactions(accountDocumentId: String?, uid: String) {
        if (accountDocumentId.isNullOrEmpty() || _state.value.selectedDate.isNullOrEmpty()) return
        viewModelScope.launch {
            when (val result = accountRepository.getUsersDailyTransactions(
                accountDocumentId = accountDocumentId,
                uid = uid,
                dateString = _state.value.selectedDate.toString()
            )) {
                is Resource.Error -> {
                    Napier.e("Sohan Error in fetching account transactions: ${result.message}")
                    _state.update {
                        it.copy(
                            latestTransactions = emptyList()
                        )
                    }
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    _state.update { it ->
                        it.copy(
                            latestTransactions = result.data?.toTransactions() ?: emptyList()
                        )
                    }
                }
            }
        }

    }

    private fun getDailyTransactions(accountDocumentId: String?) {
        if (accountDocumentId.isNullOrEmpty() || _state.value.selectedDate.isNullOrEmpty()) return
        viewModelScope.launch {
            when (val result = accountRepository.getDailyAccountTransactions(
                accountDocumentId,
                _state.value.selectedDate.toString()
            )) {
                is Resource.Error -> {
                    Napier.e("Sohan Error in fetching account transactions: ${result.message}")
                    _state.update {
                        it.copy(
                            latestTransactions = emptyList()
                        )
                    }
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    _state.update { it ->
                        it.copy(
                            latestTransactions = result.data?.toTransactions() ?: emptyList()
                        )
                    }
                }
            }
        }

    }

    private fun addAccountTransaction(accountId: String, transactionDto: TransactionDto) {
        viewModelScope.launch {
            when (val result = accountRepository.addAccountTransaction(accountId, transactionDto)) {
                is Resource.Error -> {
                    Napier.e("Sohan Error in adding account transaction: ${result.message}")
                }

                Resource.Loading -> {}
                is Resource.Success -> {
                    Napier.e("Sohan Success in adding account transaction: ${result.data}")
                }
            }
        }

    }

    private fun fetchAccounts(uid: String?) {
        if (uid == null) return
        viewModelScope.launch {
            when (val result = accountRepository.getAccounts(uid)) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            accounts = emptyList()
                        )
                    }
                }

                Resource.Loading -> {}

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            accounts = result.data ?: emptyList()
                        )
                    }
                }
            }
        }
    }

    private fun createAccount() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    selectedUsers = it.selectedUsers.toMutableList().apply {
                        it.currentUserProfile?.let { profile ->
                            add(profile)
                        }
                    }
                )
            }

            when (val result = accountRepository.addAccount(
                AccountDto(
                    accountName = _state.value.accountName,
                    initialBalance = _state.value.acInitialBalance,
                    members = _state.value.selectedUsers.toAcMemberDtos(),
                    memberIds = _state.value.selectedUsers.toMemberIds(),
                    accountId = getAccountID(),
                    createdBy = _state.value.uid,
                    createdAt = DateTimeUtils.getCurrentTimeInMilli(),
                    updatedBy = _state.value.uid,
                    updatedAt = DateTimeUtils.getCurrentTimeInMilli(),
                    creatorUserId = _state.value.uid,
                    totalExpense = 0.0,
                    totalAddition = 0.0,
                )
            )) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }

                Resource.Loading -> {
                    _state.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                }

                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    _uiEvent.send(UiEvent.NavigateToAccountHome)
                }
            }

        }
    }


    private fun fetchRegisteredPhoneNumbers() {
        viewModelScope.launch {
//            if (_state.value.matchingUsers != null) {
//                _state.update {
//                    it.copy(
//                        isLoading = false,
//                        showSheet = true
//                    )
//                }
//                return@launch
//            }
            when (val result = authRepository.fetchAllUsersPhoneNumbers()) {
                is Resource.Error -> {
                    Napier.e("Sohan Error in fetching registered phone numbers: ${result.message}")
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    val deviceContacts = contactFetcher.fetchContacts()

                    _state.update {
                        it.copy(
                            deviceContacts = deviceContacts
                        )
                    }

                    val matchedPhoneNumbers = deviceContacts
                        .map { it.phoneNumber }
                        .filter { result.data?.contains(it) == true }
                        .distinct()

                    fetchMatchingUsers(matchedPhoneNumbers)
                }
            }
        }

    }


    private suspend fun fetchMatchingUsers(phoneNumbers: List<String>) {
        when (val result = authRepository.fetchMatchingUsers(phoneNumbers)) {
            is Resource.Error -> {
                Napier.e("Sohan Error in fetching matching users: ${result.message}")
            }

            Resource.Loading -> {}
            is Resource.Success -> {
                _state.update {
                    it.copy(
                        matchingUsers = result.data.orEmpty(),
                        showSheet = true
                    )
                }
            }
        }
    }

}