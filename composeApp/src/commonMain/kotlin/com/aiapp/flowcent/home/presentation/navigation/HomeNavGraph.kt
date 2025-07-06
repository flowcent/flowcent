/*
 * Created by Saeedus Salehin on 15/5/25, 2:50 PM.
 */

package com.aiapp.flowcent.home.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.core.platform.SpeechRecognizer
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.home.presentation.screens.AuthScreen
import com.aiapp.flowcent.home.presentation.screens.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeNavGraph(
    modifier: Modifier = Modifier,
    startDestination: HomeNavRoutes
) {
    val localNavController = rememberNavController()
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsState()

    NavHost(
        navController = localNavController,
        startDestination = startDestination.route
    ) {
        addAnimatedComposable(route = HomeNavRoutes.HomeScreen.route) {
            HomeScreen(
                modifier = modifier,
                homeViewModel = viewModel,
                homeState = state,
                navController = localNavController
            )
        }

        addAnimatedComposable(route = HomeNavRoutes.AuthScreen.route) {
            AuthScreen(
                modifier = modifier,
                homeViewModel = viewModel,
                homeState = state,
                navController = localNavController
            )
        }
    }
}