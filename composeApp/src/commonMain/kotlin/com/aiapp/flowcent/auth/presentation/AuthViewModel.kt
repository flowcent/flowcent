package com.aiapp.flowcent.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.util.Resource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val prefRepository: PrefRepository
) : ViewModel() {
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

            is UserAction.CreateNewUser -> {
                createNewUserToDb(action.user)
            }

            UserAction.IsLoggedIn -> {}
            is UserAction.IsUserExist -> {}
            is UserAction.SaveUserUid -> {
                viewModelScope.launch {
                    prefRepository.saveUid(action.uid)
                }
            }
        }
    }

    private fun createNewUserToDb(user: User) {
        viewModelScope.launch {
            when (val result = authRepository.createNewUser(user)) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            uid = user.uid
                        )
                    }
                    _uiEvent.send(UiEvent.NavigateToHome)
                    println("Sohan ${result.data}")
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    println("Sohan ${result.message}")
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