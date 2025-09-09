package com.aiapp.flowcent.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.core.data.repository.PrefRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val prefRepository: PrefRepository
) : ViewModel() {
    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    fun onAction(action: UserAction) {
        when (action) {
            UserAction.Next -> moveNext()
            is UserAction.GoTo -> _state.update { it.copy(currentPage = action.page.coerceIn(0, it.totalPages - 1)) }
            UserAction.Skip, UserAction.Complete -> markSeen()
        }
    }

    private fun moveNext() {
        _state.update { s ->
            val next = (s.currentPage + 1).coerceAtMost(s.totalPages - 1)
            s.copy(currentPage = next)
        }
    }

    private fun markSeen() {
        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            prefRepository.setHasSeenOnboarding(true)
            _state.update { it.copy(isSaving = false) }
        }
    }
}

