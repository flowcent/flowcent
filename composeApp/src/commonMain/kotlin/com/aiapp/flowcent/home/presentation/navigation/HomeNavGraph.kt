/*
 * Created by Saeedus Salehin on 15/5/25, 2:50 PM.
 */

package com.aiapp.flowcent.home.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.home.presentation.UiEvent
import com.aiapp.flowcent.home.presentation.screens.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.addHomeGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    addAnimatedComposable(route = AppNavRoutes.Home.route) {
        val viewModel = koinViewModel<HomeViewModel>()
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.uiEvent.collect {
                when (it) {
                    is UiEvent.Navigate -> navController.navigate(it.route)
                    is UiEvent.ShowSnackbar -> {}
                }
            }
        }

        HomeScreen(
            modifier = modifier,
            homeViewModel = viewModel,
            homeState = state
        )
    }
}