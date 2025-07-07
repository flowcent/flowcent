package com.aiapp.flowcent.auth.presentation.navigation

sealed class AuthNavRoutes(val route: String) {
    data object AuthScreen : AuthNavRoutes("auth_screen")
}