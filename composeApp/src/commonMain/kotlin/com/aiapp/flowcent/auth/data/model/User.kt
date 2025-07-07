package com.aiapp.flowcent.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("uid")
    val uid: String,
    @SerialName("name")
    val name: String,
    @SerialName("email")
    val email: String,
    @SerialName("phoneNumber")
    val phoneNumber: String,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("providerId")
    val providerId: String,
    @SerialName("sign_in_type")
    val signInType: String,
    )