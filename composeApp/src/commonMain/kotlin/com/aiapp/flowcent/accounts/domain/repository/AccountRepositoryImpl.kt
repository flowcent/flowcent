package com.aiapp.flowcent.accounts.domain.repository

import com.aiapp.flowcent.accounts.data.model.CreateAccountDto
import com.aiapp.flowcent.accounts.data.repository.AccountRepository
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.accounts.domain.toAccounts
import com.aiapp.flowcent.util.Resource
import dev.gitlive.firebase.firestore.FirebaseFirestore
import io.github.aakira.napier.Napier

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
            Napier.e("Sohan getAccounts userId: $userId")
            val userAccountsQuery = accountsRef
                .where {
                    "creatorUserId" equalTo userId
                }
                .get()

            val memberAccountsQuery = accountsRef
                .where {
                    "memberIds" contains userId
                }
                .get()

            Napier.e("Sohan getAccounts userAccountsQuery: ${userAccountsQuery.documents}")


            val allDocuments = (userAccountsQuery.documents + memberAccountsQuery.documents)
                .distinctBy { it.id }

            val accountList = allDocuments.map { document ->
                document.data(CreateAccountDto.serializer())
            }

            Resource.Success(accountList.toAccounts())

        } catch (ex: Exception) {
            Napier.e("Sohan getAccounts error: ${ex.message}")
            Resource.Error(ex.message.toString())
        }
    }
}