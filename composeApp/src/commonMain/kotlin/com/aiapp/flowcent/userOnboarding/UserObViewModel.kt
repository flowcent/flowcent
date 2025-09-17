package com.aiapp.flowcent.userOnboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.userOnboarding.navigation.UserObNavRoutes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class UserObViewModel : ViewModel() {
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
        }
    }
}