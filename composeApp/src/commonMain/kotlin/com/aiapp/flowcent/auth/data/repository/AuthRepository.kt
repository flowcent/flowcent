package com.aiapp.flowcent.auth.data.repository

import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.core.domain.utils.Resource
import dev.gitlive.firebase.auth.AuthResult

interface AuthRepository {
    suspend fun createNewUser(user: User): Resource<String>
    suspend fun isUserExist(uid: String): Resource<Boolean>
    suspend fun fetchUserProfile(uid: String): Resource<User>
    suspend fun fetchAllUsersPhoneNumbers(): Resource<List<String>>
    suspend fun fetchMatchingUsers(phoneNumbers: List<String>): Resource<List<User>>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Resource<AuthResult>
    suspend fun signUpWithEmailAndPassword(email: String, password: String): Resource<AuthResult>
    suspend fun updateUserField(uid: String, field: String, value: Any): Resource<String>
    suspend fun updateUserSubscription(
        uid: String,
        currentPlan: String,
        currentPlanId: String,
        expiryDate: Long,
        revenueCatDeviceId: String,
        revenueCatAppUserId: String
    ): Resource<String>

}