package com.aiapp.flowcent.auth.presentation.navigation

import UserOnboardingScreen
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
import com.aiapp.flowcent.auth.presentation.screen.ChatOnboardScreen
import com.aiapp.flowcent.auth.presentation.screen.ChatWelcomeScreen
import com.aiapp.flowcent.auth.presentation.screen.CongratsScreen
import com.aiapp.flowcent.auth.presentation.screen.UserWelcomeScreen
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
            ) { modifier, authViewModel, state ->
                SignInScreen(
                    modifier = modifier,
                    authViewModel = authViewModel,
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
            ) { modifier, authViewModel, state ->
                SignUpScreen(
                    modifier = modifier,
                    authViewModel = authViewModel,
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
            ) { modifier, authViewModel, state ->
                BasicIntroScreen(
                    modifier = modifier,
                    authViewModel = authViewModel,
                    authState = state
                )
            }

        }

        addAnimatedComposable(route = AuthNavRoutes.ProfileScreen.route) { navBackStackEntry ->
            val authNavGraphEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppNavRoutes.Auth.route)
            }
            val viewModel = koinViewModel<AuthViewModel>(
                viewModelStoreOwner = authNavGraphEntry
            )

            val subscriptionVM = koinViewModel<SubscriptionViewModel>()

            BaseScreen(
                navController = navController,
                viewModel = viewModel,
                modifier = modifier,
            ) { modifier, authViewModel, state ->
                ProfileScreen(
                    modifier = modifier,
                    authViewModel = authViewModel,
                    authState = state,
                    subscriptionVM = subscriptionVM
                )
            }
        }

        addAnimatedComposable(route = AuthNavRoutes.UserWelcomeScreen.route) { navBackStackEntry ->
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
            ) { modifier, authViewModel, _ ->
                UserWelcomeScreen(
                    modifier = modifier,
                    viewModel = authViewModel
                )
            }
        }


        addAnimatedComposable(route = AuthNavRoutes.UserOnboardingScreen.route) { navBackStackEntry ->
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
            ) { modifier, authViewModel, state ->
                UserOnboardingScreen(
                    modifier = modifier,
                    viewModel = authViewModel,
                    state = state
                )
            }
        }


        addAnimatedComposable(route = AuthNavRoutes.ChatOnboardScreen.route) { navBackStackEntry ->
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
            ) { modifier, authViewModel, state ->
                ChatOnboardScreen(
                    modifier = modifier,
                    viewModel = authViewModel,
                    state = state
                )
            }
        }

        addAnimatedComposable(route = AuthNavRoutes.ChatWelcomeScreen.route) { navBackStackEntry ->
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
            ) { modifier, authViewModel, state ->
                ChatWelcomeScreen(
                    modifier = modifier,
                    viewModel = authViewModel,
                )
            }
        }


        addAnimatedComposable(route = AuthNavRoutes.CongratsScreen.route) { navBackStackEntry ->
            val authNavGraphEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppNavRoutes.Auth.route)
            }
            val viewModel = koinViewModel<AuthViewModel>(
                viewModelStoreOwner = authNavGraphEntry
            )

            val subscriptionVM = koinViewModel<SubscriptionViewModel>()

            BaseScreen(
                navController = navController,
                viewModel = viewModel,
                modifier = modifier,
            ) { modifier, authViewModel, state ->
                CongratsScreen(
                    modifier = modifier,
                    viewModel = authViewModel,
                    state = state,
                    subscriptionVM = subscriptionVM
                )
            }
        }
    }
}