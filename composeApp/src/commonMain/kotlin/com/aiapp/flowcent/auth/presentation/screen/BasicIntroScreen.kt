package com.aiapp.flowcent.auth.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.core.presentation.components.AppButton
import com.aiapp.flowcent.core.presentation.components.AppCountryPickerPhoneField
import com.aiapp.flowcent.core.presentation.components.CustomBalanceSlider
import com.aiapp.flowcent.core.presentation.components.LabeledInputField
import io.github.aakira.napier.Napier

@Composable
fun BasicIntroScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    authState: AuthState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Spacer(Modifier.height(48.dp))

                        Text(
                            text = "We need some basics to start with you",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "No worries! It's just a formality",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(Modifier.height(48.dp))

                        // Nick name
                        LabeledInputField(
                            label = "What should we call you?",
                            placeholder = "Please type your nick name",
                            value = authState.username,
                            onValueChange = { authViewModel.onAction(UserAction.UpdateUsername(it)) },
                        )

                        Spacer(Modifier.height(24.dp))

                        // Initial Balance
                        LabeledInputField(
                            label = "Your balance to start with?",
                            placeholder = "Add an initial balance",
                            isNumeric = true,
                            value = if (authState.initialBalance > 0.0) {
                                if (authState.initialBalance % 1.0 == 0.0) {
                                    authState.initialBalance.toInt()
                                        .toString() // Remove .0 for whole numbers
                                } else {
                                    authState.initialBalance.toString()
                                }
                            } else "",
                            onValueChange = {
                                authViewModel.onAction(
                                    UserAction.UpdateInitialBalance(
                                        it.toDouble()
                                    )
                                )
                            },
                        )

                        Spacer(Modifier.height(24.dp))

                        // Saving Goals
                        Text(
                            text = "How much do you want to save of it monthly?",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        CustomBalanceSlider(
                            openingBalance = authState.initialBalance,
                            onSliderValueChange = { amount ->
                                authViewModel.onAction(UserAction.UpdateSavingTarget(amount))
                            }
                        )

                        Spacer(Modifier.height(24.dp))


                        // Phone number
                        Text(
                            text = "Please provide us your mobile number",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        AppCountryPickerPhoneField(
                            mobileNumber = authState.phoneNumber,
                            defaultCountryCode = "us",
                            onMobileNumberChange = { newValue ->
                                authViewModel.onAction(UserAction.UpdatePhoneNumber(newValue))
                            },
                            onCountrySelected = { country ->
                                authViewModel.onAction(UserAction.UpdateCountry(country))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            label = {
                                Text(
                                    "Mobile Number",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            textFieldColors = TextFieldDefaults.colors(
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                            )
                        )

                    }
                }
            }

            // Continue Button
            Column(modifier = Modifier.fillMaxWidth()) {
                AppButton(
                    onClick = { authViewModel.onAction(UserAction.CreateNewUser) },
                    text = "Continue",
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    isLoading = authState.isEmailSignInProcessing
                )
            }
        }
    }
}