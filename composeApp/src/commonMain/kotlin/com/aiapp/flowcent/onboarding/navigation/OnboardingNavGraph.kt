package com.aiapp.flowcent.onboarding.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import com.aiapp.flowcent.onboarding.OnboardingViewModel
import com.aiapp.flowcent.onboarding.OnboardingScreen
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.addOnboardingGraph(
    navController: NavHostController
) {
    addAnimatedComposable(route = AppNavRoutes.Onboarding.route) { navBackStackEntry ->
        val navEntry = remember(navBackStackEntry) {
            navController.getBackStackEntry(AppNavRoutes.Onboarding.route)
        }

        val viewModel = koinViewModel<OnboardingViewModel>(
            viewModelStoreOwner = navEntry
        )

        OnboardingScreen(
            viewModel = viewModel,
            onFinished = {
                navController.navigate(AppNavRoutes.Auth.route) {
                    popUpTo(AppNavRoutes.Onboarding.route) { inclusive = true }
                }
            }
        )
    }
}

