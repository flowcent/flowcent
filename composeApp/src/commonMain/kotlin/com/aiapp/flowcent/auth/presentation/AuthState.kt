package com.aiapp.flowcent.auth.presentation

import dev.gitlive.firebase.auth.FirebaseUser
import com.aiapp.flowcent.core.presentation.components.countryCodePicker.model.CountryDetails

data class AuthState(
    val isLoading: Boolean = false,
    val uid: String = "",
    val initialBalance: String = "",
    val firebaseUser: FirebaseUser? = null,
    val signInType: String = "",
    val phoneNumber: String = "",
    val countryDetails: CountryDetails? = null,
    val registeredPhoneNumbersCache: Set<String>? = null
)
