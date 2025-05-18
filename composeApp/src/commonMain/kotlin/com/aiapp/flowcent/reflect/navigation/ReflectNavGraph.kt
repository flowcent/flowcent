/*
 * Created by Saeedus Salehin on 15/5/25, 2:49 PM.
 */

package com.aiapp.flowcent.reflect.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.core.navigation.addAnimatedComposable
import com.aiapp.flowcent.reflect.presentation.ReflectHomeScreen

@Composable
fun ReflectNavGraph(
    startDestination: ReflectNavRoutes
) {
    val localNavController = rememberNavController()

    NavHost(
        navController = localNavController,
        startDestination = startDestination.route
    ) {
        addAnimatedComposable(route = ReflectNavRoutes.ReflectHome.route) {
            ReflectHomeScreen()
        }
    }
}