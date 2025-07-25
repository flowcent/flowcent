package com.aiapp.flowcent.auth.domain.repository

import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.util.Resource
import dev.gitlive.firebase.firestore.Filter
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.WhereConstraint

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

    override suspend fun fetchAllUsersPhoneNumbers(): Resource<List<String>> {
        return try {
            val allUsersSnapshot = userCollection.get()
            val allUsersPhoneNumbers = allUsersSnapshot.documents.map {
                it.data(User.serializer()).phoneNumber
            }
            Resource.Success(allUsersPhoneNumbers)
        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun fetchMatchingUsers(phoneNumbers: List<String>): Resource<List<User>> {
        if (phoneNumbers.isEmpty()) {
            return Resource.Success(emptyList())
        }
        return try {
            val querySnapshot = userCollection
                .where {
                    "phoneNumber" inArray phoneNumbers
                }
                .get()

            val users = querySnapshot.documents.map { it.data<User>() }

            Resource.Success(users)
        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }
}