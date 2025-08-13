/*
 * Created by Saeedus Salehin on 26/11/24, 10:36 PM.
 */

package com.aiapp.flowcent.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavGraph
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavRoutes
import com.aiapp.flowcent.auth.presentation.navigation.AuthNavGraph
import com.aiapp.flowcent.auth.presentation.navigation.AuthNavRoutes
import com.aiapp.flowcent.chat.presentation.navigation.ChatNavGraph
import com.aiapp.flowcent.chat.presentation.navigation.ChatNavRoutes
import com.aiapp.flowcent.core.presentation.platform.SpeechRecognizer
import com.aiapp.flowcent.home.presentation.navigation.HomeNavGraph
import com.aiapp.flowcent.home.presentation.navigation.HomeNavRoutes
import com.aiapp.flowcent.splash.navigation.SplashNavGraph
import com.aiapp.flowcent.splash.navigation.SplashNavRoutes

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    startDestination: AppNavRoutes = AppNavRoutes.Auth,
    speechRecognizer: SpeechRecognizer
) {
    NavHost(
        navController = AppNavController.current,
        startDestination = startDestination.route
    ) {
        composable(route = AppNavRoutes.Splash.route) {
            SplashNavGraph(
                modifier = modifier,
                startDestination = SplashNavRoutes.SplashScreen,
            )
        }

        composable(route = AppNavRoutes.Auth.route) {
            AuthNavGraph(
                modifier = modifier,
                startDestination = AuthNavRoutes.SignInScreen,
            )
        }

        composable(route = AppNavRoutes.Accounts.route) {
            AccountsNavGraph(
                modifier = modifier,
                startDestination = AccountsNavRoutes.AccountsHomeScreen
            )
        }

        composable(route = AppNavRoutes.Home.route) {
            HomeNavGraph(
                modifier = modifier,
                startDestination = HomeNavRoutes.HomeScreen
            )
        }

        composable(route = AppNavRoutes.Chat.route) {
            ChatNavGraph(
                modifier = modifier,
                startDestination = ChatNavRoutes.ChatScreen,
                speechRecognizer = speechRecognizer
            )
        }

        composable(route = AppNavRoutes.Profile.route) {
            AuthNavGraph(
                modifier = modifier,
                startDestination = AuthNavRoutes.ProfileScreen,
            )
        }
    }
}