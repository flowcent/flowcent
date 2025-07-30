package com.aiapp.flowcent.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedButton
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
import com.aiapp.flowcent.core.presentation.components.AppCountryPickerPhoneField
import com.aiapp.flowcent.core.presentation.components.NumericInputField
import com.aiapp.flowcent.core.presentation.ui.theme.Colors

@Composable
fun CongratsScreen(
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
        modifier = Modifier.padding(
            horizontal = 16.dp,
            vertical = 16.dp
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Congratulations! ${authState.firebaseUser?.displayName}, You got premium for 1 month",
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        AppCountryPickerPhoneField(
            mobileNumber = authState.phoneNumber,
            defaultCountryCode = "ad",
            onMobileNumberChange = { newValue ->
                authViewModel.onAction(UserAction.UpdatePhoneNumber(newValue))
            },
            onCountrySelected = { country ->
                authViewModel.onAction(UserAction.UpdateCountry(country))
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Mobile Number") }
        )


        NumericInputField(
            value = authState.initialBalance,
            onValueChange = { newValue ->
                authViewModel.onAction(UserAction.UpdateInitialBalance(newValue))
            },
            placeholder = "Initial Balancee"
        )

        OutlinedButton(
            onClick = { authViewModel.onAction(UserAction.CreateNewUser) },
            modifier = Modifier.padding(top = 20.dp)

        ) {
            Text("Proceed")
        }
    }
}