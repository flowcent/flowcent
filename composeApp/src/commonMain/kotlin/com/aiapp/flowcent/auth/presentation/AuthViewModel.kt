package com.aiapp.flowcent.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel() : ViewModel() {
    private val _state = MutableStateFlow(AuthState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    fun onAction(action: UserAction) {
        when (action) {
            UserAction.NavigateToHome -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateToHome)
                }
            }

            UserAction.FirebaseSignOut -> {
                viewModelScope.launch {
                    googleSignOut()
                }
            }
        }
    }

    private suspend fun googleSignOut() {
        println("Sohan Firebase.auth.currentUser ${Firebase.auth.currentUser}")
        if (Firebase.auth.currentUser != null) {
            Firebase.auth.signOut()
        }
    }
}