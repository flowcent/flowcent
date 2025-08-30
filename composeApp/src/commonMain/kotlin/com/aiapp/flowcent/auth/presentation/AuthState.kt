package com.aiapp.flowcent.auth.presentation

import com.aiapp.flowcent.auth.data.model.User
import dev.gitlive.firebase.auth.FirebaseUser
import com.aiapp.flowcent.core.presentation.components.countryCodePicker.model.CountryDetails

data class AuthState(
    val isEmailSignInProcessing: Boolean = false,
    val isGoogleSignInProcessing: Boolean = false,
    val isAppleSignInProcessing: Boolean = false,
    val uid: String = "",
    val initialBalance: Double = 0.0,
    val firebaseUser: FirebaseUser? = null,
    val signInType: String = "",
    val phoneNumber: String = "",
    val countryDetails: CountryDetails? = null,
    val registeredPhoneNumbersCache: Set<String>? = null,
    val username: String = "",
    val savingTarget: Double = 0.0,
    val user: User? = null,
    val hasUserSignedIn: Boolean = false,
    val showPaymentSheet: Boolean = false,
)
