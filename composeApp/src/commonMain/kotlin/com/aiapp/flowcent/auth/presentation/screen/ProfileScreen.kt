package com.aiapp.flowcent.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.auth.presentation.components.Events
import com.aiapp.flowcent.core.presentation.components.AppButton
import com.aiapp.flowcent.core.presentation.components.ButtonStyle
import com.aiapp.flowcent.core.presentation.ui.theme.Colors
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.compose_multiplatform

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    authState: AuthState,
    localNavController: NavController,
    globalNavController: NavController
) {
    Events(
        authViewModel = authViewModel,
        globalNavController = globalNavController,
        localNavController = localNavController
    )

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Text(
                text = "Hello ${authState.firebaseUser?.displayName}, this is your profile",
                color = Colors.Black,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Column {
            AppButton(
                btnText = "Sign Out",
                onClick = { authViewModel.onAction(UserAction.FirebaseSignOut) },
                style = ButtonStyle.PRIMARY
            )
        }
    }

}
