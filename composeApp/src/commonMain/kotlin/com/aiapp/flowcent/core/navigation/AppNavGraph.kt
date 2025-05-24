/*
 * Created by Saeedus Salehin on 26/11/24, 10:36 PM.
 */

package com.aiapp.flowcent.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavGraph
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavRoutes
import com.aiapp.flowcent.home.presentation.navigation.HomeNavGraph
import com.aiapp.flowcent.reflect.presentation.navigation.ReflectNavGraph
import com.aiapp.flowcent.reflect.presentation.navigation.ReflectNavRoutes
import com.aiapp.flowcent.home.presentation.navigation.HomeNavRoutes

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: AppNavRoutes = AppNavRoutes.Home
) {

    Box(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startDestination.route
        ) {
            composable(route = AppNavRoutes.Accounts.route) {
                AccountsNavGraph(
                    startDestination = AccountsNavRoutes.AccountsHome
                )
            }

            composable(route = AppNavRoutes.Home.route) {
                HomeNavGraph(
                    startDestination = HomeNavRoutes.HomeScreen
                )
            }

            composable(route = AppNavRoutes.Reflect.route) {
                ReflectNavGraph(
                    startDestination = ReflectNavRoutes.ReflectHome
                )
            }
        }
    }
}