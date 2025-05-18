/*
 * Created by Saeedus Salehin on 15/5/25, 12:50 PM.
 */

package com.aiapp.flowcent.core.navigation

sealed class AppNavRoutes(val route: String) {
    data object Accounts : AppNavRoutes("accounts")
    data object Transaction : AppNavRoutes("transaction")
    data object Reflect : AppNavRoutes("reflect")
}