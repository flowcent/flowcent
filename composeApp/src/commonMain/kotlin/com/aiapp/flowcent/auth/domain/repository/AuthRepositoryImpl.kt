package com.aiapp.flowcent.auth.domain.repository

import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.util.Resource
import dev.gitlive.firebase.firestore.FirebaseFirestore

class AuthRepositoryImpl(
    firestore: FirebaseFirestore
) : AuthRepository {

    private val userCollection = firestore.collection("user")

    override suspend fun createNewUser(user: User): Resource<String> {
        return try {
            userCollection
                .document(user.uid)
                .set(user)
            Resource.Success("User created successfully")
        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun isUserExist(uid: String): Resource<Boolean> {
        return try {
            val snapShot = userCollection.document(uid).get()
            if (snapShot.exists) {
                return Resource.Success(true)
            } else {
                return Resource.Success(false)
            }
        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun fetchUserProfile(uid: String): Resource<User> {
        return try {
            val snapShot = userCollection.document(uid).get()
            if (snapShot.exists) {
                Resource.Success(snapShot.data(User.serializer()))
            } else {
                return Resource.Error("User does not exist")
            }
        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }
}