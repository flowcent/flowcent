package com.aiapp.flowcent.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val uid: String,
    val name: String,
    val email: String,
    val phoneNumber: String,
    val imageUrl: String,
    val initialBalance: Double,
    val providerId: String,
    val signInType: String,
    val createdBy: String,
    val createdAt: String,
    val updatedAt: String,
    val updatedBy: String
)