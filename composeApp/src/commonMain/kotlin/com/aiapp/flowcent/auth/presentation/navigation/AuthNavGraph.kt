package com.aiapp.flowcent.auth.presentation.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.screen.BaseScreen
import com.aiapp.flowcent.auth.presentation.screen.BasicIntroScreen
import com.aiapp.flowcent.auth.presentation.screen.ProfileScreen
import com.aiapp.flowcent.auth.presentation.screen.SignInScreen
import com.aiapp.flowcent.auth.presentation.screen.SignUpScreen
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import com.aiapp.flowcent.subscription.presentation.SubscriptionViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.addAuthGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigation(
        route = AppNavRoutes.Auth.route,
        startDestination = AuthNavRoutes.SignInScreen.route
    ) {
        addAnimatedComposable(route = AuthNavRoutes.SignInScreen.route) { navBackStackEntry ->
            val authNavGraphEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppNavRoutes.Auth.route)
            }
            val viewModel = koinViewModel<AuthViewModel>(
                viewModelStoreOwner = authNavGraphEntry
            )

            BaseScreen(
                navController = navController,
                viewModel = viewModel,
                modifier = modifier,
            ) { modifier, viewModel, state ->
                SignInScreen(
                    modifier = modifier,
                    authViewModel = viewModel,
                    authState = state,
                )
            }
        }

        addAnimatedComposable(route = AuthNavRoutes.SignUpScreen.route) { navBackStackEntry ->
            val authNavGraphEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppNavRoutes.Auth.route)
            }
            val viewModel = koinViewModel<AuthViewModel>(
                viewModelStoreOwner = authNavGraphEntry
            )

            BaseScreen(
                navController = navController,
                viewModel = viewModel,
                modifier = modifier,
            ) { modifier, viewModel, state ->
                SignUpScreen(
                    modifier = modifier,
                    authViewModel = viewModel,
                    authState = state,
                )
            }
        }

        addAnimatedComposable(route = AuthNavRoutes.BasicIntroScreen.route) { navBackStackEntry ->
            val authNavGraphEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppNavRoutes.Auth.route)
            }
            val viewModel = koinViewModel<AuthViewModel>(
                viewModelStoreOwner = authNavGraphEntry
            )

            BaseScreen(
                navController = navController,
                viewModel = viewModel,
                modifier = modifier,
            ) { modifier, viewModel, state ->
                BasicIntroScreen(
                    modifier = modifier,
                    authViewModel = viewModel,
                    authState = state
                )
            }

        }

        addAnimatedComposable(route = AuthNavRoutes.ProfileScreen.route) { navBackStackEntry ->
            val authNavGraphEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppNavRoutes.Home.route)
            }
            val viewModel = koinViewModel<AuthViewModel>(
                viewModelStoreOwner = authNavGraphEntry
            )

            val subscriptionVM = koinViewModel<SubscriptionViewModel>()

            BaseScreen(
                navController = navController,
                viewModel = viewModel,
                modifier = modifier,
            ) { modifier, viewModel, state ->
                ProfileScreen(
                    modifier = modifier,
                    authViewModel = viewModel,
                    authState = state,
                    subscriptionVM = subscriptionVM
                )
            }
        }
    }
}