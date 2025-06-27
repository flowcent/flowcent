/*
 * Created by Saeedus Salehin on 24/5/25, 9:07 PM.
 */

package com.aiapp.flowcent.home.presentation

import com.aiapp.flowcent.data.request.ExpenseItem

data class HomeState(
    val isLoading: Boolean = false,
    val latestTransactions: List<ExpenseItem> = emptyList()
)