package com.aiapp.flowcent.splash.navigation

import SplashScreen
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import com.aiapp.flowcent.splash.SplashViewModel
import com.aiapp.flowcent.splash.UserAction
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.addSplashGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    addAnimatedComposable(route = AppNavRoutes.Splash.route) {
        val viewModel = koinViewModel<SplashViewModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.onAction(UserAction.FetchUidFromStore)
        }

        LaunchedEffect(key1 = state.isUiLoaded) {
            if (state.isUiLoaded) {
                if (state.uid.isNotEmpty()) {
                    navController.navigate(AppNavRoutes.Home.route) {
                        popUpTo(AppNavRoutes.Splash.route) { inclusive = true }
                    }
                } else {
                    navController.navigate(AppNavRoutes.Auth.route) {
                        popUpTo(AppNavRoutes.Splash.route) { inclusive = true }
                    }
                }
            }
        }

        SplashScreen(
            modifier = modifier,
            state = state,
            viewModel = viewModel
        )
    }
}