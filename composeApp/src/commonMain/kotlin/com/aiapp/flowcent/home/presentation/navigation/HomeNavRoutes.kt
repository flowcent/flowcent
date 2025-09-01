/*
 * Created by Saeedus Salehin on 15/5/25, 2:50 PM.
 */

package com.aiapp.flowcent.home.presentation.navigation

sealed class HomeNavRoutes(val route: String) {
    data object HomeScreen : HomeNavRoutes("home_screen")
}