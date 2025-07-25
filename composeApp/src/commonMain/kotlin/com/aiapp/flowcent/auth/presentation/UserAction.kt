package com.aiapp.flowcent.auth.presentation

import dev.gitlive.firebase.auth.FirebaseUser

sealed interface UserAction {
    data object NavigateToHome : UserAction
    data object FirebaseSignOut : UserAction
    data class IsUserExist(val firebaseUser: FirebaseUser, val signInType: String) : UserAction
    data object IsLoggedIn : UserAction
    data object CreateNewUser : UserAction
    data class SaveUserUid(val uid: String) : UserAction
    data class UpdateUserName(val userName: String) : UserAction
    data class UpdateInitialBalance(val initialBalance: String) : UserAction
    data class UpdatePhoneNumber(val phoneNumber: String) : UserAction
    data object FetchRegisteredPhoneNumbers : UserAction
}