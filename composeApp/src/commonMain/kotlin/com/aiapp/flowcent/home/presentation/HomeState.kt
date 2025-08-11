/*
 * Created by Saeedus Salehin on 24/5/25, 9:07 PM.
 */

package com.aiapp.flowcent.home.presentation

import com.aiapp.flowcent.core.domain.model.ExpenseItem

data class HomeState(
    val isLoading: Boolean = true,
    val latestTransactions: List<List<ExpenseItem>> = emptyList(),
    val selectedDate: String? = null,
    val uid: String = ""
)