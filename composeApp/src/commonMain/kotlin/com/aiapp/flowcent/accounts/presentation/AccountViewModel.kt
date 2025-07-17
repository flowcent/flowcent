package com.aiapp.flowcent.accounts.presentation

import androidx.lifecycle.ViewModel
import com.aiapp.flowcent.accounts.data.repository.AccountRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

class AccountViewModel(
    private val accountRepository: AccountRepository,
    private val prefRepository: PrefRepository
) : ViewModel()  {
    private val _state = MutableStateFlow(AccountState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.AddAccount -> TODO()
        }
    }

}