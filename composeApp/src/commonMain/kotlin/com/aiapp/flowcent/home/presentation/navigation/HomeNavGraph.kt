/*
 * Created by Saeedus Salehin on 15/5/25, 2:50 PM.
 */

package com.aiapp.flowcent.home.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.LocalNavController
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.home.presentation.UiEvent
import com.aiapp.flowcent.home.presentation.screens.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeNavGraph(
    modifier: Modifier = Modifier,
    startDestination: HomeNavRoutes
) {
    val localNavController = rememberNavController()
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsState()
    val globalNavController = LocalNavController.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect {
            when (it) {
                UiEvent.NavigateToAuth -> globalNavController.navigate(AppNavRoutes.Auth.route)
                is UiEvent.ShowSnackbar -> {}
                UiEvent.NavigateToProfile -> globalNavController.navigate(AppNavRoutes.Profile.route)
            }
        }
    }

    CompositionLocalProvider(LocalNavController provides localNavController) {
        NavHost(
            navController = localNavController,
            startDestination = startDestination.route
        ) {
            addAnimatedComposable(route = HomeNavRoutes.HomeScreen.route) {
                HomeScreen(
                    modifier = modifier,
                    homeViewModel = viewModel,
                    homeState = state
                )
            }
        }
    }
}