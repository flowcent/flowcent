package com.aiapp.flowcent.auth.presentation

import com.aiapp.flowcent.core.presentation.components.countryCodePicker.model.CountryDetails
import dev.gitlive.firebase.auth.FirebaseUser

sealed interface UserAction {
    data object FirebaseSignOut : UserAction
    data class IsUserExist(val firebaseUser: FirebaseUser, val signInType: String) : UserAction
    data object IsLoggedIn : UserAction
    data object CreateNewUser : UserAction
    data class SaveUserUid(val uid: String) : UserAction
    data class UpdateInitialBalance(val initialBalance: Double) : UserAction
    data class UpdatePhoneNumber(val phoneNumber: String) : UserAction
    data class UpdateCountry(val countryDetails: CountryDetails) : UserAction
    data class UpdateUsername(val username: String) : UserAction
    data object NavigateToSignIn : UserAction
    data object NavigateToSignUp : UserAction
    data class UpdateSavingTarget(val amount: Double) : UserAction
    data class OnGoogleSignInResult(val result: Result<FirebaseUser?>) : UserAction

    data object FetchUserId : UserAction
    data class SignInWithEmailPass(
        val email: String,
        val password: String
    ) : UserAction

    data class SignUpWithEMailPass(
        val email: String,
        val password: String,
        val confirmPassword: String
    ) : UserAction

    data class ShowPaymentSheet(val sheetState: Boolean) : UserAction

    data object NavigateToUserOnboard : UserAction
    data object NavigateToChatOnboard : UserAction
    data class UpdateText(val text: String) : UserAction
    data class SendMessage(val text: String) : UserAction
    data object ResetChatState : UserAction
    data object NavigateToChatWelcome : UserAction
    data class UpdateInputErrors(val errors: Map<String, String>) : UserAction
    data object NavigateToHome : UserAction
    data object NavigateBack : UserAction
}