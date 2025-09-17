package com.aiapp.flowcent.userOnboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserObViewModel : ViewModel() {
    private val _state = MutableStateFlow(UserOnboardingState())
    val state = _state.asStateFlow()


}