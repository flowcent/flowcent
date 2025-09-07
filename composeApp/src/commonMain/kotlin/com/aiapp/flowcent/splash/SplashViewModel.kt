package com.aiapp.flowcent.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.core.data.repository.PrefRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val prefRepository: PrefRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state = _state.asStateFlow()

    fun onAction(action: UserAction) {
        when (action) {
            UserAction.FetchUidFromStore -> fetchUidFromStore()
        }
    }

    private fun fetchUidFromStore() {
        viewModelScope.launch {
            val uidFromDataStore = prefRepository.uid.first()
            if (uidFromDataStore.isNullOrEmpty()) {
                _state.update { currentState ->
                    currentState.copy(
                        isUiLoaded = true,
                        shouldShowBottomNav = false
                    )
                }
            } else {
                _state.update { currentState ->
                    currentState.copy(
                        uid = uidFromDataStore,
                        isUiLoaded = true,
                        shouldShowBottomNav = true
                    )
                }
            }
        }
    }
}