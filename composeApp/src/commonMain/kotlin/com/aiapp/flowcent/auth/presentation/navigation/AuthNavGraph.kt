package com.aiapp.flowcent.auth.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UiEvent
import com.aiapp.flowcent.auth.presentation.screen.BasicIntroScreen
import com.aiapp.flowcent.auth.presentation.screen.ProfileScreen
import com.aiapp.flowcent.auth.presentation.screen.SignInScreen
import com.aiapp.flowcent.auth.presentation.screen.SignUpScreen
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.addAuthGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    addAnimatedComposable(route = AppNavRoutes.Auth.route) {
        val viewModel = koinViewModel<AuthViewModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    UiEvent.NavigateToHome -> {
                        navController.navigate(AppNavRoutes.Home.route)
                    }
                    UiEvent.NavigateToBasicIntro -> {
                        navController.navigate(AuthNavRoutes.BasicIntroScreen.route)
                    }
                    UiEvent.NavigateToSignIn -> {
                        navController.navigate(AuthNavRoutes.SignInScreen.route)
                    }
                    UiEvent.NavigateToSignUp -> {
                        navController.navigate(AuthNavRoutes.SignUpScreen.route)
                    }
                }
            }
        }

        SignInScreen(
            modifier = modifier,
            authViewModel = viewModel,
            authState = state
        )
    }

    addAnimatedComposable(route = AuthNavRoutes.SignUpScreen.route) {
        val viewModel = koinViewModel<AuthViewModel>()
        val state by viewModel.state.collectAsState()
        SignUpScreen(
            modifier = modifier,
            authViewModel = viewModel,
            authState = state
        )
    }

    addAnimatedComposable(route = AuthNavRoutes.BasicIntroScreen.route) {
        val viewModel = koinViewModel<AuthViewModel>()
        val state by viewModel.state.collectAsState()
        BasicIntroScreen(
            modifier = modifier,
            authViewModel = viewModel,
            authState = state
        )
    }

    addAnimatedComposable(route = AuthNavRoutes.ProfileScreen.route) {
        val viewModel = koinViewModel<AuthViewModel>()
        val state by viewModel.state.collectAsState()
        ProfileScreen(
            modifier = modifier,
            authViewModel = viewModel,
            authState = state
        )
    }
}