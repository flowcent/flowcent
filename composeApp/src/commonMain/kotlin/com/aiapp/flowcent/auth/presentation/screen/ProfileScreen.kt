package com.aiapp.flowcent.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.core.presentation.components.AppButton

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    authState: AuthState
) {
    LaunchedEffect(Unit) {
        authViewModel.onAction(UserAction.FetchUserId)
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Text(
                text = "Hello ${authState.user?.localUserName}, this is your profile",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Column {
            AppButton(
                onClick = {
                    authViewModel.onAction(UserAction.FirebaseSignOut)
                },
                text = "Sign Out",
                backgroundColor = Color(0xFF0E0E0E),
                isLoading = authState.isEmailSignInProcessing
            )
        }
    }

}
