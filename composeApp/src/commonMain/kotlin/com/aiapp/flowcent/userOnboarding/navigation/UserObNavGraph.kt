package com.aiapp.flowcent.userOnboarding.navigation

import UserOnboardingScreen
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import com.aiapp.flowcent.userOnboarding.UserObViewModel
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.addUserObNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigation(
        route = AppNavRoutes.UserOnboarding.route,
        startDestination = UserObNavRoutes.UserOnboardingScreen.route
    ) {
        addAnimatedComposable(route = UserObNavRoutes.UserOnboardingScreen.route) { navBackStackEntry ->
            val userObNavGraphEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppNavRoutes.UserOnboarding.route)
            }

            val viewModel = koinViewModel<UserObViewModel>(
                viewModelStoreOwner = userObNavGraphEntry
            )

            val state by viewModel.state.collectAsState()

            UserOnboardingScreen(
                modifier = modifier,
                viewModel = viewModel,
                state = state
            )
        }
    }
}