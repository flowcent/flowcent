/*
 * Created by Saeedus Salehin on 13/8/25, 12:10 PM.
 */

package com.aiapp.flowcent.core.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun GetTopBarForRoute(navController: NavHostController, route: String) {
    when (route) {
        AppNavRoutes.Accounts.route -> AppBar(navController = navController, title = "Shared")
        AppNavRoutes.Auth.route -> null
        AppNavRoutes.Chat.route -> AppBar(navController = navController, title = "Chat")

        AppNavRoutes.Home.route -> null
        AppNavRoutes.Profile.route -> null
        AppNavRoutes.Splash.route -> null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    navController: NavHostController,
    title: String,
    showBackButton: Boolean = true,
    actions: @Composable (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.inverseSurface
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.inverseSurface
                    )
                }
            } else null
        },
        actions = { actions }
    )
}