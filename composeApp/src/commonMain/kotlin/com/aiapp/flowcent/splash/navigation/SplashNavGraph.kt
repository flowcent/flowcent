package com.aiapp.flowcent.splash.navigation

import SplashScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.LocalNavController
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import com.aiapp.flowcent.splash.SplashViewModel
import com.aiapp.flowcent.splash.UserAction
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SplashNavGraph(
    modifier: Modifier = Modifier,
    globalNavController: NavHostController,
    startDestination: SplashNavRoutes,
) {
    val localNavController = rememberNavController()
    val viewModel = koinViewModel<SplashViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onAction(UserAction.FetchUidFromStore)
    }

    LaunchedEffect(key1 = state.isUiLoaded) {
        if (state.isUiLoaded) {
            if (state.uid.isNotEmpty()) {
                globalNavController.navigate(AppNavRoutes.Home.route) {
                    popUpTo(AppNavRoutes.Splash.route) { inclusive = true }
                }
            } else {
                globalNavController.navigate(AppNavRoutes.Auth.route) {
                    popUpTo(AppNavRoutes.Splash.route) { inclusive = true }
                }
            }
        }
    }

    CompositionLocalProvider(LocalNavController provides localNavController) {
        NavHost(
            navController = localNavController,
            startDestination = startDestination.route
        ) {
            addAnimatedComposable(route = SplashNavRoutes.SplashScreen.route) {
                SplashScreen(
                    modifier = modifier,
                    state = state,
                    viewModel = viewModel
                )
            }
        }
    }
}