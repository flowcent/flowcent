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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import com.aiapp.flowcent.core.presentation.components.AppCountryPickerPhoneField
import com.aiapp.flowcent.core.presentation.components.CustomBalanceSlider

@Composable
fun BasicIntroScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    authState: AuthState
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawWithCache {
                onDrawBehind {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFECE8FD),
                                Color(0xFFFFFFFF),
                                Color(0xFFEBF9FE),
                                Color(0xFFF9E8FA),
                            )
                        )
                    )
                }
            }
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(72.dp))

                Text(
                    text = "We need some basics to start with you",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "No worries! It's just a formality",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )

                Spacer(Modifier.height(48.dp))

                // Username
                Text(text = "What should we call you?", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))

                OutlinedTextField(
                    value = authState.username,
                    onValueChange = { authViewModel.onAction(UserAction.UpdateUsername(it)) },
                    placeholder = { Text("Type Your Usual Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(24.dp))

                // Opening Balance
                Text(
                    text = "Your balance to begin with?",
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(6.dp))

                OutlinedTextField(
                    value = if (authState.initialBalance > 0.0) {
                        if (authState.initialBalance % 1.0 == 0.0) {
                            authState.initialBalance.toInt()
                                .toString() // Remove .0 for whole numbers
                        } else {
                            authState.initialBalance.toString()
                        }
                    } else "",
                    onValueChange = { authViewModel.onAction(UserAction.UpdateInitialBalance(it.toDouble())) },
                    placeholder = { androidx.compose.material.Text("Opening Balance") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )

                Spacer(Modifier.height(24.dp))

                // Saving Goals
                Text(
                    text = "How much do you want to save of it monthly?",
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(6.dp))


                CustomBalanceSlider(
                    openingBalance = authState.initialBalance,
                    onSliderValueChange = { amount ->
                        authViewModel.onAction(UserAction.UpdateSavingTarget(amount))
                    }
                )

                Spacer(Modifier.height(24.dp))


                // Phone number
                Text(text = "Please provide us your phone number", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))

                AppCountryPickerPhoneField(
                    mobileNumber = authState.phoneNumber,
                    defaultCountryCode = "us",
                    onMobileNumberChange = { newValue ->
                        authViewModel.onAction(UserAction.UpdatePhoneNumber(newValue))
                    },
                    onCountrySelected = { country ->
                        authViewModel.onAction(UserAction.UpdateCountry(country))
                    },
                    modifier = Modifier.fillMaxWidth()
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                    label = { Text("Mobile Number", style = MaterialTheme.typography.bodyMedium) }
                )

            }

            // Next Button
            Column(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { authViewModel.onAction(UserAction.CreateNewUser) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0E0E0E))
                ) {
                    Text(
                        text = "Next",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                    )
                }
            }
        }
    }
}