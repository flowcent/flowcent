package com.aiapp.flowcent.core.domain.repository

import com.aiapp.flowcent.chat.util.ChatUtil.getCurrentFormattedDateTime
import com.aiapp.flowcent.core.data.repository.ExpenseRepository
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.util.Resource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore

class ExpenseRepositoryImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore
) : ExpenseRepository {
    private val transactionCollection = firestore.collection("transactions")

    override suspend fun saveExpenseItemsToDb(expenseItems: List<ExpenseItem>): Resource<String> {
        return try {
            if (expenseItems.isEmpty()) {
                return Resource.Error("No expense items provided to save.")
            }

            // save the first item as per your original code
            val firstExpenseItem = expenseItems[0]

            val transaction = hashMapOf(
                "amount" to firstExpenseItem.amount,
                "category" to firstExpenseItem.category,
                "created_at" to getCurrentFormattedDateTime(),
                "created_by" to "Sohan", // Consider making this dynamic (e.g., from user session)
                "title" to firstExpenseItem.title,
                "tn_id" to "tqcMiL3tg3jvWqqhHJ4I", // Consider if this should be dynamic or removed
                "type" to "Expense",
                "updated_at" to getCurrentFormattedDateTime(),
                "updated_by" to "Sohan", // Consider making this dynamic
            )

            val addDocRef = transactionCollection
                .add(transaction)

            Resource.Success(addDocRef.id)
        } catch (e: Exception) {
            println("Sohan error in adding doc: ${e.message}")
            Resource.Error("Failed to save expense: ${e.message}")
        }
    }

    override suspend fun getAllExpenses(): Resource<List<ExpenseItem>> {
        return try {
            val querySnapshot = transactionCollection
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

}