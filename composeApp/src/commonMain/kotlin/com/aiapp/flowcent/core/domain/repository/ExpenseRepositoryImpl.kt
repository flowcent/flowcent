package com.aiapp.flowcent.core.domain.repository

import com.aiapp.flowcent.core.data.model.TransactionDto
import com.aiapp.flowcent.core.data.repository.ExpenseRepository
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getStartAndEndTimeMillis
import com.aiapp.flowcent.core.domain.utils.Resource
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore

class ExpenseRepositoryImpl(
    firestore: FirebaseFirestore
) : ExpenseRepository {
    private val transactionCollection = firestore.collection("transactions")

    override suspend fun saveExpenseItemsToDb(
        uid: String,
        transactionDto: TransactionDto
    ): Resource<String> {
        return try {
            if (transactionDto.expenses.isEmpty()) {
                return Resource.Error("No expense items provided to save.")
            }

            val addDocRef = transactionCollection
                .add(transactionDto)

            Resource.Success(addDocRef.id)
        } catch (e: Exception) {
            println("Sohan error in adding doc: ${e.message}")
            Resource.Error("Failed to save expense: ${e.message}")
        }
    }

    override suspend fun getAllExpenses(uid: String): Resource<List<ExpenseItem>> {
        return try {
            val querySnapshot = transactionCollection
                .where {
                    "uid" equalTo uid
                }
                .orderBy("created_at", Direction.DESCENDING)
                .get()
            val expenseList = querySnapshot.documents.map { document ->
                document.data(ExpenseItem.serializer())
            }
            Resource.Success(expenseList)
        } catch (e: Exception) {
            Resource.Error("Error fetching expenses: ${e.message}")
        }

    }

    override suspend fun getDailyExpenses(
        uid: String,
        dateString: String
    ): Resource<List<TransactionDto>> {
        return try {
            val (start, end) = getStartAndEndTimeMillis(dateString)
            val querySnapshot = transactionCollection
                .where { "uid" equalTo uid }
                .where { "createdAt" greaterThanOrEqualTo start }
                .where { "createdAt" lessThanOrEqualTo end }
                .orderBy("createdAt", Direction.DESCENDING)
                .get()

            val transactions = querySnapshot.documents.map { document ->
                val dto = document.data(TransactionDto.serializer())
                dto.copy(id = document.id)
            }

            Resource.Success(transactions)
        } catch (e: Exception) {
            Resource.Error("Sohan Error fetching expenses: ${e.message}")
        }

    }

    override suspend fun totalAmount(uid: String): Resource<Double> {
        return try {
            val snapshot = transactionCollection
                .where { "uid" equalTo uid }
                .get()
            val total = snapshot.documents.sumOf {
                it.data(TransactionDto.serializer()).totalAmount
            }
            Resource.Success(total)
        } catch (e: Exception) {
            Resource.Error("Error fetching expenses: ${e.message}")
        }
    }

    override suspend fun totalExpenses(uid: String): Resource<Double> {
        return try {
            val snapshot = transactionCollection
                .where { "uid" equalTo uid }
                .where { "type" equalTo "Expense" }
                .get()
            val total = snapshot.documents.sumOf {
                it.data(ExpenseItem.serializer()).amount
            }
            Resource.Success(total)
        } catch (e: Exception) {
            Resource.Error("Error fetching expenses: ${e.message}")
        }
    }

    override suspend fun totalIncome(uid: String): Resource<Double> {
        return try {
            val snapshot = transactionCollection
                .where { "uid" equalTo uid }
                .where { "type" equalTo "Income" }
                .get()
            val total = snapshot.documents.sumOf {
                it.data(ExpenseItem.serializer()).amount
            }
            Resource.Success(total)
        } catch (e: Exception) {
            Resource.Error("Error fetching expenses: ${e.message}")
        }
    }

}