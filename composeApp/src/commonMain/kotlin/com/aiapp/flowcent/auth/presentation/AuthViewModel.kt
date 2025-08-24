package com.aiapp.flowcent.auth.presentation

import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.domain.utils.Constants
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils
import com.aiapp.flowcent.core.domain.utils.Resource
import com.aiapp.flowcent.core.presentation.platform.ConnectivityObserver
import com.aiapp.flowcent.core.utils.DialogType
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

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
            UserAction.FirebaseSignOut -> {
                viewModelScope.launch {
                    if (Firebase.auth.currentUser != null) {
                        Firebase.auth.signOut()
                    }
                    prefRepository.deleteUid()
                    _uiEvent.send(UiEvent.NavigateToSignIn)
                }
            }

            is UserAction.CreateNewUser -> {
                createNewUserToDb(
                    User(
                        uid = _state.value.firebaseUser?.uid ?: "",
                        fullName = _state.value.firebaseUser?.displayName ?: "",
                        email = _state.value.firebaseUser?.email ?: "",
                        phoneNumber = _state.value.countryDetails?.countryPhoneNumberCode + _state.value.phoneNumber,
                        imageUrl = _state.value.firebaseUser?.photoURL ?: "",
                        initialBalance = _state.value.initialBalance,
                        providerId = _state.value.firebaseUser?.providerId ?: "",
                        signInType = _state.value.signInType,
                        createdBy = _state.value.firebaseUser?.uid ?: "",
                        createdAt = DateTimeUtils.getCurrentTimeInMilli(),
                        updatedAt = DateTimeUtils.getCurrentTimeInMilli(),
                        updatedBy = _state.value.firebaseUser?.uid ?: "",
                        localUserName = _state.value.username,
                        savingTarget = _state.value.savingTarget
                    )
                )
            }

            UserAction.IsLoggedIn -> {}
            is UserAction.IsUserExist -> {
                viewModelScope.launch {
                    when (val result = authRepository.isUserExist(action.firebaseUser.uid)) {
                        is Resource.Error -> {
                            setLoaderOff(action.signInType)
                        }

                        is Resource.Loading -> {}
                        is Resource.Success -> {
                            setLoaderOff(action.signInType)
                            _state.update {
                                it.copy(
                                    firebaseUser = action.firebaseUser,
                                    signInType = action.signInType
                                )
                            }
                            if (result.data == true) {
                                setUserSignedIn(true)
                                saveUserUid(action.firebaseUser.uid)
                                _uiEvent.send(UiEvent.NavigateToHome)
                            } else {
                                _uiEvent.send(UiEvent.NavigateToBasicIntro)
                            }
                        }
                    }
                }
            }

            is UserAction.SaveUserUid -> saveUserUid(action.uid)
            is UserAction.UpdateInitialBalance -> {
                if (!action.initialBalance.toString().matches(Regex("^\\d*\\.?\\d*\$"))) return
                if (action.initialBalance < 0) return
                if (action.initialBalance > 10000000) return

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            initialBalance = action.initialBalance
                        )
                    }
                }
            }

            is UserAction.UpdatePhoneNumber -> {
                Napier.e("Sohan action.phoneNumber ${action.phoneNumber}")
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            phoneNumber = action.phoneNumber
                        )
                    }
                }
            }

            is UserAction.UpdateCountry -> {
                Napier.e("Sohan action.countryDetails ${action.countryDetails}")
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            countryDetails = action.countryDetails
                        )
                    }
                }
            }

            is UserAction.UpdateUsername -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            username = action.username
                        )
                    }
                }
            }

            UserAction.NavigateToSignIn -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateToSignIn)
                }
            }

            UserAction.NavigateToSignUp -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateToSignUp)
                }
            }

            is UserAction.UpdateSavingTarget -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            savingTarget = action.amount
                        )
                    }
                }
            }

            is UserAction.OnGoogleSignInResult -> onGoogleSignInResult(action.result)

            UserAction.FetchUserId -> {
                viewModelScope.launch {
                    prefRepository.uid.collect { uidFromDataStore ->
                        _state.update { currentState ->
                            currentState.copy(uid = uidFromDataStore ?: "")
                        }
                        fetchUserProfile(uid = uidFromDataStore)
                    }
                }
            }

            is UserAction.CheckInternet -> {
                viewModelScope.launch {
                    if (action.status == ConnectivityObserver.Status.Unavailable) {
                        _uiEvent.send(UiEvent.ShowDialog(dialogType = DialogType.NO_INTERNET))
                    }
                }
            }

            is UserAction.SignInWithEmailPass -> signInWithEmailPass(
                action.email,
                action.password,
                Constants.SIGN_IN_TYPE_EMAILPASS
            )

            is UserAction.SignUpWithEMailPass -> {
                signUpWithEmailPass(
                    action.email.trim(),
                    action.password.trim(),
                    action.confirmPassword.trim(),
                    Constants.SIGN_IN_TYPE_EMAILPASS
                )
            }

            is UserAction.ShowPaymentSheet -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            showPaymentSheet = action.sheetState
                        )
                    }
                }
            }
        }
    }

    private fun saveUserUid(uid: String) {
        viewModelScope.launch {
            prefRepository.saveUid(uid)
        }
    }

    private fun setUserSignedIn(hasSignedIn: Boolean) {
        _state.update {
            it.copy(
                hasUserSignedIn = hasSignedIn
            )

        }
    }

    private fun fetchUserProfile(uid: String?) {
        println("Sohan fetchUserProfile _state.value.uid ${_state.value.uid}")
        if (uid.isNullOrEmpty()) {
            Napier.e("Sohan 404 No User Found")
            return
        }
        viewModelScope.launch {
            when (val result = authRepository.fetchUserProfile(uid)) {
                is Resource.Error -> {
                    println("Sohan Error in fetching user profile: ${result.message}")
                }

                Resource.Loading -> {}
                is Resource.Success -> {
                    println("Sohan Success in fetching user profile: ${result.data?.localUserName}")
                    _state.update {
                        it.copy(
                            user = result.data
                        )
                    }
                }
            }

        }
    }

    private fun setLoaderOn(signInType: String) {
        _state.update {
            it.copy(
                isEmailSignInProcessing = when (signInType) {
                    Constants.SIGN_IN_TYPE_EMAILPASS -> true
                    else -> it.isEmailSignInProcessing
                },
                isGoogleSignInProcessing = when (signInType) {
                    Constants.SIGN_IN_TYPE_GOOGLE -> true
                    else -> it.isGoogleSignInProcessing
                },
                isAppleSignInProcessing = when (signInType) {
                    Constants.SIGN_IN_TYPE_APPLE -> true
                    else -> it.isAppleSignInProcessing
                },
            )
        }
    }


    private fun setLoaderOff(signInType: String) {
        _state.update {
            it.copy(
                isEmailSignInProcessing = when (signInType) {
                    Constants.SIGN_IN_TYPE_EMAILPASS -> false
                    else -> it.isEmailSignInProcessing
                },
                isGoogleSignInProcessing = when (signInType) {
                    Constants.SIGN_IN_TYPE_GOOGLE -> false
                    else -> it.isGoogleSignInProcessing
                },
                isAppleSignInProcessing = when (signInType) {
                    Constants.SIGN_IN_TYPE_APPLE -> false
                    else -> it.isAppleSignInProcessing
                },
            )
        }
    }


    private fun signInWithEmailPass(email: String, password: String, signInType: String) {
        viewModelScope.launch {
            setLoaderOn(signInType)
            when (val result = authRepository.signInWithEmailAndPassword(email, password)) {
                is Resource.Error -> {
                    Napier.e("Sohan result.message ${result.message}")
                    setLoaderOff(signInType)
                    _uiEvent.send(
                        UiEvent.ShowDialog(
                            dialogType = DialogType.ERROR,
                            title = "Sign In Failed",
                            body = if (result.message
                                    .contentEquals(
                                        "The supplied auth credential is incorrect, malformed or has expired.",
                                        ignoreCase = true
                                    )
                            )
                                "The email or password is incorrect, or has expired."
                            else
                                "Sorry! Something went wrong. Please try again later",
                        )
                    )
                }

                Resource.Loading -> {
                    setLoaderOn(signInType)
                }

                is Resource.Success -> {
                    result.data?.let {
                        val firebaseUser = it.user
                        if (firebaseUser != null) {
                            onAction(UserAction.IsUserExist(firebaseUser, signInType))
                        }
                    }
                }
            }
        }
    }


    private fun signUpWithEmailPass(
        email: String,
        password: String,
        confirmPassword: String,
        signInType: String
    ) {
        viewModelScope.launch {
            setLoaderOn(signInType)
            if (password != confirmPassword) {
                setLoaderOff(signInType)
                _uiEvent.send(
                    UiEvent.ShowDialog(
                        dialogType = DialogType.ERROR,
                        title = "Password and Confirm Password does not match",
                    )
                )
            }
            when (val result =
                authRepository.signUpWithEmailAndPassword(email, password)) {
                is Resource.Error -> {
                    Napier.e("Sohan result.message ${result.message}")
                    setLoaderOff(signInType)
                    _uiEvent.send(
                        UiEvent.ShowDialog(
                            dialogType = DialogType.ERROR,
                            title = "Sign In Failed",
                            body = if (result.message
                                    .contentEquals(
                                        "The supplied auth credential is incorrect, malformed or has expired.",
                                        ignoreCase = true
                                    )
                            )
                                "The email or password is incorrect, or has expired."
                            else
                                "Sorry! Something went wrong. Please try again later",
                        )
                    )
                }

                Resource.Loading -> {
                    setLoaderOn(signInType)
                }

                is Resource.Success -> {
                    result.data?.let {
                        val firebaseUser = it.user
                        if (firebaseUser != null) {
                            onAction(UserAction.IsUserExist(firebaseUser, signInType))
                        }
                    }
                }
            }
        }
    }


    private fun onGoogleSignInResult(result: Result<FirebaseUser?>) {
        viewModelScope.launch {
            try {
                if (result.isSuccess) {
                    val firebaseUser = result.getOrNull()
                    if (firebaseUser != null) {
                        onAction(
                            UserAction.IsUserExist(firebaseUser, Constants.SIGN_IN_TYPE_GOOGLE)
                        )
                    }
                } else {
                    if (result.exceptionOrNull() is CancellationException) {
                        Napier.e("Google Sign-In was cancelled by the user.")
                    } else {
                        Napier.e("onFirebaseResult Error: ${result.exceptionOrNull()?.message}")
                        if (result.exceptionOrNull()?.message?.contains("Idtoken is null") == true) {
                            _uiEvent.send(
                                UiEvent.ShowDialog(
                                    dialogType = DialogType.NO_INTERNET,
                                )
                            )
                        }
                    }
                }
            } catch (ex: Exception) {
                Napier.e("onFirebaseResult Exception: ${ex.message}")
            }
        }
    }

    private fun createNewUserToDb(user: User) {
        viewModelScope.launch {
            when (val result = authRepository.createNewUser(user)) {
                is Resource.Loading -> {}

                is Resource.Success -> {
                    println("Sohan createNewUserToDb success ${result.data}")
                    setUserSignedIn(true)
                    saveUserUid(user.uid)
                    _uiEvent.send(UiEvent.NavigateToHome)
                }

                is Resource.Error -> {
                    setUserSignedIn(false)
                    println("Sohan createNewUserToDb ${result.message}")
                }
            }
        }
    }
}