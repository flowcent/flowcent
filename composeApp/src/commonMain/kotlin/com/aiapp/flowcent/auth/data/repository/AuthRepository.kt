package com.aiapp.flowcent.auth.data.repository

import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.core.domain.utils.Resource

interface AuthRepository {
    suspend fun createNewUser(user: User): Resource<String>
    suspend fun isUserExist(uid: String): Resource<Boolean>
    suspend fun fetchUserProfile(uid: String): Resource<User>
    suspend fun fetchAllUsersPhoneNumbers(): Resource<List<String>>
    suspend fun fetchMatchingUsers(phoneNumbers: List<String>): Resource<List<User>>
}