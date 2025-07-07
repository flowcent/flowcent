package com.aiapp.flowcent.auth.presentation

import com.aiapp.flowcent.auth.data.model.User

sealed interface UserAction {
    data object NavigateToHome : UserAction
    data object FirebaseSignOut : UserAction
    data class IsUserExist(val uid: String) : UserAction
    data object IsLoggedIn : UserAction
    data class CreateNewUser(
        val user: User
    ) : UserAction
}