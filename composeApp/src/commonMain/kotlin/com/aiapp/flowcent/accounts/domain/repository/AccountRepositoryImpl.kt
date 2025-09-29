package com.aiapp.flowcent.accounts.domain.repository

import com.aiapp.flowcent.accounts.data.model.AccountDto
import com.aiapp.flowcent.accounts.data.repository.AccountRepository
import com.aiapp.flowcent.accounts.domain.model.Account
import com.aiapp.flowcent.accounts.domain.utils.toAccounts
import com.aiapp.flowcent.core.domain.utils.Resource
import com.aiapp.flowcent.core.data.model.TransactionDto
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentMonthStartAndEndMillis
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getStartAndEndTimeMillis
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore
import io.github.aakira.napier.Napier

class AccountRepositoryImpl(
    firestore: FirebaseFirestore
) : AccountRepository {

    private val accountsRef = firestore.collection("accounts")

    private fun getTransactionsCollection(accountId: String): CollectionReference {
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

    override suspend fun addAccountTransaction(
        accountDocumentId: String,
        transactionDto: TransactionDto
    ): Resource<String> {
        return try {
            val accountTransactionsRef = getTransactionsCollection(accountDocumentId)
            val addTransactionRef = accountTransactionsRef
                .add(transactionDto)
            Resource.Success(addTransactionRef.id)
        } catch (ex: Exception) {
            Napier.e("Sohan addAccountTransaction error: ${ex.message}")
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun getDailyAccountTransactions(
        accountDocumentId: String,
        dateString: String
    ): Resource<List<TransactionDto>> {
        return try {
            val (start, end) = getStartAndEndTimeMillis(dateString)
            val accountTransactionsRef = getTransactionsCollection(accountDocumentId)
            val accountTransactionsQuery =
                accountTransactionsRef
                    .where { "createdAt" greaterThanOrEqualTo start }
                    .where { "createdAt" lessThanOrEqualTo end }
                    .orderBy("createdAt", Direction.DESCENDING).get()

            val accountTransactions = accountTransactionsQuery.documents.map { document ->
                document.data(TransactionDto.serializer())
            }
            Resource.Success(accountTransactions)
        } catch (ex: Exception) {
            Napier.e("Sohan getAccountTransactions error: ${ex.message}")
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun getUsersDailyTransactions(
        accountDocumentId: String,
        uid: String,
        dateString: String
    ): Resource<List<TransactionDto>> {
        return try {
            val (start, end) = getStartAndEndTimeMillis(dateString)
            val accountTransactionsRef = getTransactionsCollection(accountDocumentId)
            val accountTransactionsQuery =
                accountTransactionsRef
                    .where { "createdBy" equalTo uid }
                    .where { "createdAt" greaterThanOrEqualTo start }
                    .where { "createdAt" lessThanOrEqualTo end }
                    .orderBy("createdAt", Direction.DESCENDING).get()

            val accountTransactions = accountTransactionsQuery.documents.map { document ->
                document.data(TransactionDto.serializer())
            }
            Resource.Success(accountTransactions)
        } catch (ex: Exception) {
            Napier.e("Sohan getAccountTransactions error: ${ex.message}")
            Resource.Error(ex.message.toString())
        }
    }

    override suspend fun totalMonthlyAmount(accountDocumentId: String): Resource<Double> {
        return try {
            val (start, end) = getCurrentMonthStartAndEndMillis()
            val accountTransactionsRef = getTransactionsCollection(accountDocumentId)
            val accountTransactionsQuery =
                accountTransactionsRef
                    .where { "createdAt" greaterThanOrEqualTo start }
                    .where { "createdAt" lessThanOrEqualTo end }
                    .orderBy("createdAt", Direction.DESCENDING).get()

            val total = accountTransactionsQuery.documents.sumOf {
                it.data(TransactionDto.serializer()).totalAmount
            }
            Resource.Success(total)
        } catch (ex: Exception) {
            Napier.e("Sohan getAccountTransactions error: ${ex.message}")
            Resource.Error(ex.message.toString())
        }
    }
}