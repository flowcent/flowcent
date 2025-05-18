/*
 * Created by Saeedus Salehin on 15/5/25, 2:49 PM.
 */

package com.aiapp.flowcent.accounts.navigation

sealed class AccountsNavRoutes(val route: String) {
    data object AccountsHome : AccountsNavRoutes("accounts_home")
}