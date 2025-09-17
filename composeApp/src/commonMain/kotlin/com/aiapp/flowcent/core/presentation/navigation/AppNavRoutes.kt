/*
 * Created by Saeedus Salehin on 15/5/25, 12:50 PM.
 */

package com.aiapp.flowcent.core.presentation.navigation

sealed class AppNavRoutes(val route: String) {
    data object Splash : AppNavRoutes("splash")
    data object Onboarding : AppNavRoutes("onboarding")
    data object Auth : AppNavRoutes("auth")
    data object Accounts : AppNavRoutes("accounts")
    data object Home : AppNavRoutes("home")
    data object Chat : AppNavRoutes("chat")
    data object Profile : AppNavRoutes("profile_screen")
    data object Insights : AppNavRoutes("insights")
    data object UserOnboarding : AppNavRoutes("user_onboarding")
}
