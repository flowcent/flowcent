/*
 * Created by Saeedus Salehin on 15/5/25, 2:50 PM.
 */

package com.aiapp.flowcent.transaction.navigation

sealed class TransactionNavRoutes(val route: String) {
    data object TransactionHome : TransactionNavRoutes("transaction_home")
}