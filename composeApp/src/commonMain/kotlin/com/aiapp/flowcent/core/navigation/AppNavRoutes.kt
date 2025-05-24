/*
 * Created by Saeedus Salehin on 15/5/25, 12:50 PM.
 */

package com.aiapp.flowcent.core.navigation

sealed class AppNavRoutes(val route: String) {
    data object Accounts : AppNavRoutes("accounts")
    data object Home : AppNavRoutes("home")
    data object Reflect : AppNavRoutes("reflect")
}