/*
 * Created by Saeedus Salehin on 15/5/25, 2:50 PM.
 */

package com.aiapp.flowcent.home.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.core.navigation.addAnimatedComposable
import com.aiapp.flowcent.home.presentation.screens.HomeScreen

@Composable
fun HomeNavGraph(startDestination: HomeNavRoutes) {
    val localNavController = rememberNavController()

    NavHost(
        navController = localNavController,
        startDestination = startDestination.route
    ) {
        addAnimatedComposable(route = HomeNavRoutes.HomeScreen.route) {
            HomeScreen()
        }
    }
}