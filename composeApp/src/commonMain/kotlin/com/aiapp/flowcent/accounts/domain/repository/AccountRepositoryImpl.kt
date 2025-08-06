package com.aiapp.flowcent.accounts.domain.repository

import com.aiapp.flowcent.accounts.data.model.AccountDto
import com.aiapp.flowcent.accounts.data.repository.AccountRepository
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.accounts.domain.utils.toAccounts
import com.aiapp.flowcent.core.domain.utils.Resource
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.FirebaseFirestore
import io.github.aakira.napier.Napier

class AccountRepositoryImpl(
    firestore: FirebaseFirestore
) : AccountRepository {

    private val accountsRef = firestore.collection("accounts")

    fun getTransactionsCollection(accountId: String): CollectionReference {
        return accountsRef.document(accountId).collection("transactions")
    }

    override suspend fun addAccount(accountDto: AccountDto): Resource<String> {
        return try {
            val addAccountRef = accountsRef
                .add(accountDto)
            Resource.Success(addAccountRef.id)
        } catch (ex: Exception) {
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun getAccounts(userId: String): Resource<List<Account>> {
        return try {
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

            val allDocuments = (userAccountsQuery.documents + memberAccountsQuery.documents)
                .distinctBy { it.id }

            val accountList = allDocuments.map { document ->
                val dto = document.data(AccountDto.serializer())
                dto.copy(id = document.id)
            }

            Resource.Success(accountList.toAccounts())

        } catch (ex: Exception) {
            Napier.e("Sohan getAccounts error: ${ex.message}")
            Resource.Error(ex.message.toString())
        }
    }
}