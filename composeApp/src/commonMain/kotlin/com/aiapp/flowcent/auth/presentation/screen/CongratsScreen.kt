package com.aiapp.flowcent.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.auth.presentation.components.Events
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
            color = Colors.Black,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        OutlinedTextField(
            value = authState.userName,
            onValueChange = { newValue ->
                authViewModel.onAction(UserAction.UpdateUserName(newValue))
            },
            placeholder = { Text("User Name ") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Colors.Black,
                cursorColor = Colors.Black,
                unfocusedContainerColor = Colors.White,
                focusedContainerColor = Colors.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            maxLines = 1,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )


        OutlinedTextField(
            value = authState.phoneNumber,
            onValueChange = { newValue ->
                authViewModel.onAction(UserAction.UpdatePhoneNumber(newValue))
            },
            placeholder = { Text("Phone Number ") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Colors.Black,
                cursorColor = Colors.Black,
                unfocusedContainerColor = Colors.White,
                focusedContainerColor = Colors.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            maxLines = 1,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        )


        OutlinedTextField(
            value = authState.initialBalance,
            onValueChange = { newValue ->
                authViewModel.onAction(UserAction.UpdateInitialBalance(newValue))
            },
            placeholder = { Text("Initial Balance ") },
            colors = TextFieldDefaults.colors(
                focusedTextColor = Colors.Black,
                cursorColor = Colors.Black,
                unfocusedContainerColor = Colors.White,
                focusedContainerColor = Colors.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            maxLines = 1,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
        )


        OutlinedButton(
            onClick = { authViewModel.onAction(UserAction.CreateNewUser) },
            modifier = Modifier.padding(top = 20.dp)

        ) {
            Text("Proceed")
        }
    }
}