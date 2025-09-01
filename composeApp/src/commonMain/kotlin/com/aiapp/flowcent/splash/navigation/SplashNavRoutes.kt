package com.aiapp.flowcent.splash.navigation

sealed class SplashNavRoutes(val route: String) {
    data object SplashScreen : SplashNavRoutes("splash_screen")
}