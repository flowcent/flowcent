package com.aiapp.flowcent.auth.data.repository

import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.util.Resource

interface AuthRepository {
    suspend fun createNewUser(user: User): Resource<String>
    suspend fun isUserExist(uid: String): Resource<Boolean>
    suspend fun fetchUserProfile(uid: String): Resource<User>
}