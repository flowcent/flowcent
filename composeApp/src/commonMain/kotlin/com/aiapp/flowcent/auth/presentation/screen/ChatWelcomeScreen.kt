package com.aiapp.flowcent.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.AnimatedChatOnboarding
import com.aiapp.flowcent.core.presentation.components.AppButton

@Composable
fun ChatWelcomeScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AnimatedChatOnboarding()

        Column(
            modifier = Modifier.fillMaxWidth()
                .align(Alignment.BottomCenter).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppButton(
                hasGradient = true,
                height = 60.dp,
                text = "Start Chatting",
                onClick = { viewModel.onAction(UserAction.NavigateToChatOnboard) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}