package com.aiapp.flowcent.domain.repository

import com.aiapp.flowcent.chat.presentation.ChatUtil.getCurrentFormattedDateTime
import com.aiapp.flowcent.data.repository.ExpenseRepository
import com.aiapp.flowcent.data.request.ExpenseItem
import com.aiapp.flowcent.util.Resource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore

class ExpenseRepositoryImpl(
    private val firestore: FirebaseFirestore = Firebase.firestore
) : ExpenseRepository {
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

            val addDocRef = firestore.collection("transactions")
                .add(transaction)

            Resource.Success(addDocRef.id)
        } catch (e: Exception) {
            println("Sohan error in adding doc: ${e.message}")
            Resource.Error("Failed to save expense: ${e.message}")
        }
    }

}