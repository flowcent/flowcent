/*
 * Created by Saeedus Salehin on 26/11/24, 10:36 PM.
 */

package com.aiapp.flowcent.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavGraph
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavRoutes
import com.aiapp.flowcent.auth.presentation.navigation.AuthNavGraph
import com.aiapp.flowcent.auth.presentation.navigation.AuthNavRoutes
import com.aiapp.flowcent.chat.presentation.navigation.ChatNavGraph
import com.aiapp.flowcent.chat.presentation.navigation.ChatNavRoutes
import com.aiapp.flowcent.core.platform.SpeechRecognizer
import com.aiapp.flowcent.home.presentation.navigation.HomeNavGraph
import com.aiapp.flowcent.home.presentation.navigation.HomeNavRoutes

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    globalNavController: NavHostController,
    startDestination: AppNavRoutes = AppNavRoutes.Auth,
    speechRecognizer: SpeechRecognizer
) {
    NavHost(
        navController = globalNavController,
        startDestination = startDestination.route
    ) {
        composable(route = AppNavRoutes.Auth.route) {
            AuthNavGraph(
                modifier = modifier,
                globalNavController = globalNavController,
                startDestination = AuthNavRoutes.SignInScreen,
            )
        }

        composable(route = AppNavRoutes.Accounts.route) {
            AccountsNavGraph(
                modifier = modifier,
                globalNavController = globalNavController,
                startDestination = AccountsNavRoutes.AccountsHomeScreen
            )
        }

        composable(route = AppNavRoutes.Home.route) {
            HomeNavGraph(
                modifier = modifier,
                globalNavController = globalNavController,
                startDestination = HomeNavRoutes.HomeScreen
            )
        }

        composable(route = AppNavRoutes.Chat.route) {
            ChatNavGraph(
                modifier = modifier,
                globalNavController = globalNavController,
                startDestination = ChatNavRoutes.ChatScreen,
                speechRecognizer = speechRecognizer
            )
        }

        composable(route = AppNavRoutes.Profile.route) {
            AuthNavGraph(
                modifier = modifier,
                globalNavController = globalNavController,
                startDestination = AuthNavRoutes.ProfileScreen,
            )
        }
    }
}