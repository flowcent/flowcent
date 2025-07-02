/*
 * Created by Saeedus Salehin on 26/11/24, 10:36 PM.
 */

package com.aiapp.flowcent.core.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavGraph
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavRoutes
import com.aiapp.flowcent.chat.presentation.navigation.ChatNavGraph
import com.aiapp.flowcent.chat.presentation.navigation.ChatNavRoutes
import com.aiapp.flowcent.home.presentation.navigation.HomeNavGraph
import com.aiapp.flowcent.reflect.presentation.navigation.ReflectNavGraph
import com.aiapp.flowcent.reflect.presentation.navigation.ReflectNavRoutes
import com.aiapp.flowcent.home.presentation.navigation.HomeNavRoutes
import com.aiapp.flowcent.core.platform.SpeechRecognizer

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: AppNavRoutes = AppNavRoutes.Home,
    speechRecognizer: SpeechRecognizer
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        composable(route = AppNavRoutes.Accounts.route) {
            AccountsNavGraph(
                modifier = modifier,
                startDestination = AccountsNavRoutes.AccountsHome
            )
        }

        composable(route = AppNavRoutes.Home.route) {
            HomeNavGraph(
                modifier = modifier,
                startDestination = HomeNavRoutes.HomeScreen
            )
        }

        composable(route = AppNavRoutes.Reflect.route) {
            ReflectNavGraph(
                startDestination = ReflectNavRoutes.ReflectHome
            )
        }

        composable(route = AppNavRoutes.Chat.route) {
            ChatNavGraph(
                modifier = modifier,
                startDestination = ChatNavRoutes.ChatScreen,
                speechRecognizer = speechRecognizer
            )
        }
    }
}