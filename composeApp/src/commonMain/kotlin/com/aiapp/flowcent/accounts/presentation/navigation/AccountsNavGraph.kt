/*
 * Created by Saeedus Salehin on 15/5/25, 2:48 PM.
 */

package com.aiapp.flowcent.accounts.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.accounts.presentation.screens.AccountsHomeScreen
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable

@Composable
fun AccountsNavGraph(
    modifier: Modifier = Modifier,
    startDestination: AccountsNavRoutes
) {
    val localNavController = rememberNavController()

    NavHost(
        navController = localNavController,
        startDestination = startDestination.route
    ) {
        addAnimatedComposable(route = AccountsNavRoutes.AccountsHome.route) {
            AccountsHomeScreen(modifier = modifier)
        }
    }
}