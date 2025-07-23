package com.aiapp.flowcent.auth.presentation.navigation

sealed class AuthNavRoutes(val route: String) {
    data object SignInScreen : AuthNavRoutes("sign_in_screen")
    data object SignUpScreen : AuthNavRoutes("sign_up_screen")
    data object CongratsScreen : AuthNavRoutes("congrats_screen")
    data object ProfileScreen : AuthNavRoutes("profile_screen")
}