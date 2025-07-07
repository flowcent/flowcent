package com.aiapp.flowcent.auth.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.screen.AuthScreen
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

    NavHost(
        navController = localNavController,
        startDestination = startDestination.route
    ) {
        addAnimatedComposable(route = AuthNavRoutes.AuthScreen.route) {
            AuthScreen(
                modifier = modifier,
                authViewModel = viewModel,
                authState = state,
                localNavController = localNavController,
                globalNavController = globalNavController
            )
        }
    }
}
