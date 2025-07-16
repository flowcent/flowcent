package com.aiapp.flowcent.accounts.domain

import com.aiapp.flowcent.accounts.data.model.AccountDto
import com.aiapp.flowcent.accounts.data.repository.AccountRepository
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.util.Resource
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore

class AccountRepositoryImpl(
    firestore: FirebaseFirestore
) : AccountRepository {

    private val accountsRef = firestore.collection("accounts")

    override suspend fun addAccount(accountDto: AccountDto): Resource<String> {
        return try {
            val addAccountRef = accountsRef
                .add(accountDto)
            Resource.Success(addAccountRef.id)
        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun getAccounts(userId: String): Resource<List<AccountDto>> {
        return try {
            val querySnapshot = accountsRef
                .where {
                    "uid" equalTo userId
                }
                .orderBy("created_at", Direction.DESCENDING)
                .get()
            val expenseList = querySnapshot.documents.map { document ->
                document.data(AccountDto.serializer())
            }

            Resource.Success(expenseList)

        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }


}