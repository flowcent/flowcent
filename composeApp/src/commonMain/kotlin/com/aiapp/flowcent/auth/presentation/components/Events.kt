package com.aiapp.flowcent.auth.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UiEvent
import com.aiapp.flowcent.auth.presentation.navigation.AuthNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes

@Composable
fun Events(
    authViewModel: AuthViewModel,
    globalNavController: NavController,
    localNavController: NavController
) {
    LaunchedEffect(Unit) {
        authViewModel.uiEvent.collect { event ->
            when (event) {
                UiEvent.NavigateToHome -> {
                    globalNavController.navigate(AppNavRoutes.Home.route)
                }

                UiEvent.NavigateToCongratulations -> {
                    localNavController.navigate(AuthNavRoutes.CongratsScreen.route)
                }
            }
        }
    }
}
