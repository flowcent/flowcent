/*
 * Created by Saeedus Salehin on 15/5/25, 2:50 PM.
 */

package com.aiapp.flowcent.home.presentation.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.home.presentation.screens.BaseScreen
import com.aiapp.flowcent.home.presentation.screens.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.addHomeGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigation(
        route = AppNavRoutes.Home.route,
        startDestination = HomeNavRoutes.HomeScreen.route
    ) {
        addAnimatedComposable(route = HomeNavRoutes.HomeScreen.route) { navBackStackEntry ->
            val homeNavGraphEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppNavRoutes.Home.route)
            }

            val viewModel = koinViewModel<HomeViewModel>(
                viewModelStoreOwner = homeNavGraphEntry
            )

            BaseScreen(
                navController = navController,
                viewModel = viewModel,
                modifier = modifier,
            ) { modifier, homeViewModel, state ->
                HomeScreen(
                    modifier = modifier,
                    homeViewModel = homeViewModel,
                    homeState = state
                )
            }
        }
    }
}