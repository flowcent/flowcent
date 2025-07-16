package com.aiapp.flowcent.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils
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
                createNewUserToDb(
                    User(
                        uid = _state.value.firebaseUser?.uid ?: "",
                        name = _state.value.firebaseUser?.displayName ?: "",
                        userName = _state.value.userName,
                        email = _state.value.firebaseUser?.email ?: "",
                        phoneNumber = _state.value.firebaseUser?.phoneNumber ?: "",
                        imageUrl = _state.value.firebaseUser?.photoURL ?: "",
                        initialBalance = _state.value.initialBalance,
                        providerId = _state.value.firebaseUser?.providerId ?: "",
                        signInType = _state.value.signInType,
                        createdBy = _state.value.firebaseUser?.uid ?: "",
                        createdAt = DateTimeUtils.getCurrentFormattedDateTime(),
                        updatedAt = DateTimeUtils.getCurrentFormattedDateTime(),
                        updatedBy = _state.value.firebaseUser?.uid ?: ""
                    )
                )
            }

            UserAction.IsLoggedIn -> {}
            is UserAction.IsUserExist -> {
                viewModelScope.launch {
                    when (val result = authRepository.isUserExist(action.firebaseUser.uid)) {
                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                            println("Sohan ${result.message}")
                        }

                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    firebaseUser = action.firebaseUser
                                )
                            }
                            if (result.data == true) {
                                _uiEvent.send(UiEvent.NavigateToHome)
                                println("Sohan ${result.data}")
                            } else {
                                _uiEvent.send(UiEvent.NavigateToCongratulations)
                            }
                        }
                    }
                }
            }

            is UserAction.SaveUserUid -> {
                viewModelScope.launch {
                    prefRepository.saveUid(action.uid)
                }
            }

            is UserAction.UpdateInitialBalance -> {
                if (!action.initialBalance.matches(Regex("^\\d*\\.?\\d*\$"))) return
                if (action.initialBalance.toDouble() < 0) return
                if (action.initialBalance.toDouble() > 10000000) return

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            initialBalance = action.initialBalance
                        )
                    }
                }
            }

            is UserAction.UpdateUserName -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            userName = action.userName
                        )
                    }
                }
            }
        }
    }

    private fun createNewUserToDb(user: User) {
        viewModelScope.launch {
            when (val result = authRepository.createNewUser(user)) {
                is Resource.Loading -> {}

                is Resource.Success -> {
                    println("Sohan createNewUserToDb success ${result.data}")
                    _uiEvent.send(UiEvent.NavigateToHome)
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                    println("Sohan createNewUserToDb ${result.message}")
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