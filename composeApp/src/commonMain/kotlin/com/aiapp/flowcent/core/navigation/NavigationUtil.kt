/*
 * Created by Saeedus Salehin on 22/11/24, 6:26 PM.
 */

package com.aiapp.flowcent.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

fun NavGraphBuilder.addAnimatedComposable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        enterTransition = { defaultSlideInHorizontally(1) },
        exitTransition = { defaultSlideOutHorizontally(-1) },
        popEnterTransition = { defaultSlideInHorizontally(-1) },
        popExitTransition = { defaultSlideOutHorizontally(1) }
    ) { backStackEntry ->
        content(backStackEntry)
    }
}

@Composable
fun getCurrentRoute(navController: NavController): String {
    val currentBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = currentBackStackEntry?.destination?.route ?: ""
    return currentRoute.substringBefore("?").substringBefore("/")
}


fun defaultSlideInHorizontally(direction: Int) = slideInHorizontally(
    initialOffsetX = { fullWidth -> direction * fullWidth },
    animationSpec = tween(durationMillis = 100, delayMillis = 50)
)

fun defaultSlideOutHorizontally(direction: Int) = slideOutHorizontally(
    targetOffsetX = { fullWidth -> direction * fullWidth },
    animationSpec = tween(durationMillis = 100, delayMillis = 50)
)