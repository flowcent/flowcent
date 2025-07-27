package com.aiapp.flowcent.accounts.domain.repository

import com.aiapp.flowcent.accounts.data.model.CreateAccountDto
import com.aiapp.flowcent.accounts.data.repository.AccountRepository
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.accounts.domain.toAccounts
import com.aiapp.flowcent.util.Resource
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore

class AccountRepositoryImpl(
    firestore: FirebaseFirestore
) : AccountRepository {

    private val accountsRef = firestore.collection("accounts")

    override suspend fun addAccount(createAccountDto: CreateAccountDto): Resource<String> {
        return try {
            val addAccountRef = accountsRef
                .add(createAccountDto)
            Resource.Success(addAccountRef.id)
        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun getAccounts(userId: String): Resource<List<Account>> {
        return try {
            val querySnapshot = accountsRef
                .where {
                    "uid" equalTo userId
                }
                .orderBy("created_at", Direction.DESCENDING)
                .get()
            val expenseList = querySnapshot.documents.map { document ->
                document.data(CreateAccountDto.serializer())
            }

            Resource.Success(expenseList.toAccounts())

        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }


}