/*
 * Created by Saeedus Salehin on 15/5/25, 2:49 PM.
 */

package com.aiapp.flowcent.accounts.presentation.navigation

sealed class AccountsNavRoutes(val route: String) {
    data object AccountsHomeScreen : AccountsNavRoutes("accounts_home_screen")
    data object AddAccountScreen : AccountsNavRoutes("add_account_screen")
    data object AccountDetailScreen : AccountsNavRoutes("account_detail_screen")
}