package com.aiapp.flowcent.auth.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UiEvent
import com.aiapp.flowcent.auth.presentation.screen.CongratsScreen
import com.aiapp.flowcent.auth.presentation.screen.ProfileScreen
import com.aiapp.flowcent.auth.presentation.screen.SignInScreen
import com.aiapp.flowcent.auth.presentation.screen.SignUpScreen
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AuthNavGraph(
    modifier: Modifier = Modifier,
    globalNavController: NavHostController,
    startDestination: AuthNavRoutes,
) {
    val localNavController = rememberNavController()
    val viewModel = koinViewModel<AuthViewModel>()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.NavigateToHome -> {
                    globalNavController.navigate(AppNavRoutes.Home.route)
                }

                UiEvent.NavigateToCongratulations -> {
                    localNavController.navigate(AuthNavRoutes.CongratsScreen.route)
                }

                UiEvent.NavigateToSignIn -> {
                    localNavController.navigate(AuthNavRoutes.SignInScreen.route)
                }

                UiEvent.NavigateToSignUp -> {
                    localNavController.navigate(AuthNavRoutes.SignUpScreen.route)
                }
            }
        }
    }

    NavHost(
        navController = localNavController,
        startDestination = startDestination.route
    ) {
        addAnimatedComposable(route = AuthNavRoutes.SignInScreen.route) {
            SignInScreen(
                modifier = modifier,
                authViewModel = viewModel,
                authState = state
            )
        }

        addAnimatedComposable(route = AuthNavRoutes.SignUpScreen.route) {
            SignUpScreen(
                modifier = modifier,
                authViewModel = viewModel,
                authState = state
            )
        }


        addAnimatedComposable(route = AuthNavRoutes.CongratsScreen.route) {
            CongratsScreen(
                modifier = modifier,
                authViewModel = viewModel,
                authState = state
            )
        }

        addAnimatedComposable(route = AuthNavRoutes.ProfileScreen.route) {
            ProfileScreen(
                modifier = modifier,
                authViewModel = viewModel,
                authState = state
            )
        }
    }
}
