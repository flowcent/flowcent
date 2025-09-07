package com.aiapp.flowcent.auth.domain.repository

import com.aiapp.flowcent.auth.data.model.Subscription
import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.core.domain.utils.Resource
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.FirebaseFirestore
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

class AuthRepositoryImpl(
    firestore: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
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
                Resource.Error("User does not exist.")
            }
        } catch (ex: Exception) {
            // Log the full exception for better debugging
            Napier.e("Error fetching user profile", ex)
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

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Resource<AuthResult> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password)
            Resource.Success(result)
        } catch (ex: Exception) {
            Resource.Error(ex.message ?: "Sign in failed")
        }
    }

    override suspend fun signUpWithEmailAndPassword(
        email: String,
        password: String,
    ): Resource<AuthResult> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password)
            Resource.Success(result)
        } catch (ex: Exception) {
            Resource.Error(ex.message ?: "Sign up failed")
        }
    }

    override suspend fun updateUserField(uid: String, field: String, value: Any): Resource<String> {
        return try {
            userCollection
                .document(uid)
                .update(mapOf(field to value))
            Resource.Success("Sohan User field $field updated successfully")
        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun updateUserSubscription(
        uid: String,
        subscription: Subscription
    ): Resource<String> {
        return try {
            val userSubscription = mapOf("subscription" to subscription)
            userCollection
                .document(uid)
                .update(userSubscription)

            Resource.Success("User subscription updated successfully")
        } catch (ex: Exception) {
            Napier.e("Sohan Error in updating user subscription: ${ex.message}")
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun updateTotalRecordCredits(uid: String, credits: Int): Resource<String> {
        return try {
            val totalRecordCredits = mapOf("totalRecordCredits" to credits)
            userCollection
                .document(uid)
                .update(totalRecordCredits)

            Resource.Success("User totalRecordCredits updated successfully")
        } catch (ex: Exception) {
            Napier.e("Sohan Error in updating user totalRecordCredits: ${ex.message}")
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun updateTotalChatCredits(uid: String, credits: Int): Resource<String> {
        return try {
            val totalAiChatCredits = mapOf("totalAiChatCredits" to credits)
            userCollection
                .document(uid)
                .update(totalAiChatCredits)

            Resource.Success("User totalAiChatCredits updated successfully")
        } catch (ex: Exception) {
            Napier.e("Sohan Error in updating user totalAiChatCredits: ${ex.message}")
            Resource.Error(ex.message.toString())
        }
    }
}