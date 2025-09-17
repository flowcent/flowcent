package com.aiapp.flowcent.userOnboarding.navigation

sealed class UserObNavRoutes(val route: String) {
    data object UserOnboardingScreen : UserObNavRoutes("user_onboarding_screen")
    data object UserWelcomeScreen : UserObNavRoutes("user_welcome_screen")
}