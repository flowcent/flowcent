package com.aiapp.flowcent.auth.presentation

import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.FirebaseUser

data class AuthState(
    val isLoading: Boolean = false,
    val uid: String = "",
    val userName: String = "",
    val initialBalance: String = "",
    val firebaseUser: FirebaseUser? = null,
    val signInType: String = "",
    val credential: AuthCredential? = null,
)
