package com.aiapp.flowcent.accounts.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.accounts.data.model.CreateAccountDto
import com.aiapp.flowcent.accounts.data.repository.AccountRepository
import com.aiapp.flowcent.accounts.domain.toAcMemberDto
import com.aiapp.flowcent.accounts.domain.toAcMemberDtos
import com.aiapp.flowcent.accounts.domain.toMemberIds
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.platform.ContactFetcher
import com.aiapp.flowcent.util.Resource
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
                    if (_state.value.uid.isNotEmpty()) {
                        fetchAccounts(_state.value.uid)
                    } else {
                        prefRepository.uid.collect { uidFromDataStore ->
                            _state.update { currentState ->
                                currentState.copy(uid = uidFromDataStore ?: "")
                            }
                            fetchAccounts(uidFromDataStore)
                        }
                    }
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
            when (val result = accountRepository.addAccount(
                CreateAccountDto(
                    accountName = _state.value.accountName,
                    initialBalance = _state.value.acInitialBalance,
                    members = _state.value.selectedUsers.toAcMemberDtos(),
                    memberIds = _state.value.selectedUsers.toMemberIds(),
                    accountId = "TestId",
                    createdBy = _state.value.uid,
                    createdAt = "TestDate",
                    updatedBy = _state.value.uid,
                    updatedAt = "TestDate",
                    creatorUserId = _state.value.uid,
                )
            )) {
                is Resource.Error -> {
                    Napier.e("Sohan Error in creating account: ${result.message}")
                }

                Resource.Loading -> {}
                is Resource.Success -> {
                    Napier.e("Sohan Success in creating account: ${result.data}")
                }

            }

        }
    }


    private fun fetchRegisteredPhoneNumbers() {
        viewModelScope.launch {
            if (_state.value.matchingUsers != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        showSheet = true
                    )
                }
                return@launch
            }
            when (val result = authRepository.fetchAllUsersPhoneNumbers()) {
                is Resource.Error -> {
                    Napier.e("Sohan Error in fetching registered phone numbers: ${result.message}")
                }

                is Resource.Loading -> {}
                is Resource.Success -> {
                    Napier.e("Sohan Success in fetching registered phone numbers: ${result.data}")
                    val deviceContacts = contactFetcher.fetchContacts()
                    Napier.e("Sohan deviceContacts: $deviceContacts")
                    val matchedPhoneNumbers = deviceContacts
                        .map { it.phoneNumber }
                        .filter { result.data?.contains(it) == true }
                        .distinct()

                    Napier.e("Sohan matchedPhoneNumbers: $matchedPhoneNumbers")

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
                Napier.e("Sohan Success in fetching matching users: ${result.data}")
                _state.update {
                    it.copy(
                        matchingUsers = result.data?.toSet(),
                        showSheet = true
                    )
                }
            }
        }
    }

}