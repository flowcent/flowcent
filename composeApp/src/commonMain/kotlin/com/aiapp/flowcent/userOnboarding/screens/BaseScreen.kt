package com.aiapp.flowcent.userOnboarding.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.aiapp.flowcent.userOnboarding.UiEvent

import com.aiapp.flowcent.userOnboarding.UserObViewModel
import com.aiapp.flowcent.userOnboarding.UserOnboardingState
import com.aiapp.flowcent.userOnboarding.event.EventHandler

@Composable
fun BaseScreen(
    navController: NavController,
    viewModel: UserObViewModel,
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier, viewModel: UserObViewModel, state: UserOnboardingState) -> Unit
) {

    val state by viewModel.state.collectAsState()

    EventHandler(eventFlow = viewModel.uiEvent) { event ->
        handleEvent(event, navController)
    }

    content(modifier, viewModel, state)
}

private fun handleEvent(
    event: UiEvent,
    navController: NavController,
) {
    when (event) {
        is UiEvent.Navigate -> navController.navigate(event.route)
    }
}