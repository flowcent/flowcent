package com.aiapp.flowcent.auth.presentation.navigation

sealed class AuthNavRoutes(val route: String) {
    data object SignInScreen : AuthNavRoutes("sign_in_screen")
    data object SignUpScreen : AuthNavRoutes("sign_up_screen")
    data object BasicIntroScreen : AuthNavRoutes("basic_intro_screen")
    data object ProfileScreen : AuthNavRoutes("profile_screen")
    data object UserOnboardingScreen : AuthNavRoutes("user_onboarding_screen")
    data object UserWelcomeScreen : AuthNavRoutes("user_welcome_screen")
    data object ChatOnboardScreen : AuthNavRoutes("chat_onboard_screen")
    data object ChatWelcomeScreen : AuthNavRoutes("chat_welcome_screen")
    data object CongratsScreen : AuthNavRoutes("congrats_screen")
}