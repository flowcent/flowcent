package com.aiapp.flowcent.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val imageUrl: String,
    val initialBalance: Double,
    val providerId: String,
    val signInType: String,
    val createdBy: String,
    val createdAt: Long = 0L,
    val updatedAt: Long = 0L,
    val updatedBy: String,
    val localUserName: String,
    val savingTarget: Double = 0.0
)