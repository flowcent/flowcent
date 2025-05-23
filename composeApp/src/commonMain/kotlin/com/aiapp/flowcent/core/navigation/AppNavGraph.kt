/*
 * Created by Saeedus Salehin on 26/11/24, 10:36 PM.
 */

package com.aiapp.flowcent.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavGraph
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavRoutes
import com.aiapp.flowcent.transaction.presentation.navigation.TransactionNavGraph
import com.aiapp.flowcent.reflect.presentation.navigation.ReflectNavGraph
import com.aiapp.flowcent.reflect.presentation.navigation.ReflectNavRoutes
import com.aiapp.flowcent.transaction.presentation.navigation.TransactionNavRoutes

@Composable
fun AppNavGraph(startDestination: AppNavRoutes) {
    val navController = rememberNavController()


    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = startDestination.route
        ) {
            composable(route = AppNavRoutes.Accounts.route) {
                AccountsNavGraph(
                    startDestination = AccountsNavRoutes.AccountsHome
                )
            }

            composable(route = AppNavRoutes.Transaction.route) {
                TransactionNavGraph(
                    startDestination = TransactionNavRoutes.TransactionHome
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