package com.aiapp.flowcent.auth.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.auth.presentation.component.TermsAndConditionsText
import com.aiapp.flowcent.core.Spacing
import com.aiapp.flowcent.core.presentation.components.AppButton
import com.aiapp.flowcent.core.presentation.components.LabeledInputField
import com.aiapp.flowcent.core.utils.PRIVACY_POLICY_URL
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.apple.AppleButtonMode
import com.mmk.kmpauth.uihelper.apple.AppleSignInButton
import com.mmk.kmpauth.uihelper.google.GoogleButtonMode
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    authState: AuthState
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(
                    vertical = 12.dp
                ).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(48.dp))

                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Just a few quick things to get started",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(Modifier.height(48.dp))

                // Email Field
                // Email Field
                LabeledInputField(
                    label = "Email ID",
                    placeholder = "Please type your email",
                    value = email,
                    onValueChange = { email = it },
                )

                Spacer(Modifier.height(24.dp))

                // Password Field
                // Password Field
                LabeledInputField(
                    label = "Password",
                    placeholder = "Please type a strong password",
                    value = password,
                    onValueChange = { password = it },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon =
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(icon, contentDescription = null)
                        }
                    }
                )
                Spacer(Modifier.height(16.dp))

                // Confirm Password Field
                // Password Field
                LabeledInputField(
                    label = "Confirm Password",
                    placeholder = "Please confirm your password",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon =
                            if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(icon, contentDescription = null)
                        }
                    }
                )

                Spacer(Modifier.height(Spacing.xxLarge))

                //Terms and condition
                TermsAndConditionsText(
                    url = PRIVACY_POLICY_URL,
                    modifier = Modifier.fillMaxWidth().padding(start = Spacing.small)
                )

                Spacer(Modifier.height(Spacing.large))

                // Sign Up Button
                AppButton(
                    onClick = {
                        authViewModel.onAction(
                            UserAction.SignUpWithEMailPass(
                                email, password, confirmPassword
                            )
                        )
                    },
                    text = "Sign Up",
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    isLoading = authState.isEmailSignInProcessing
                )

                Spacer(Modifier.height(32.dp))

                // Or with Divider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "  Or with  ",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Divider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(Modifier.height(32.dp))

                GoogleButtonUiContainerFirebase(
                    linkAccount = false,
                    filterByAuthorizedAccounts = false,
                    onResult = { result ->
                        authViewModel.onAction(
                            UserAction.OnGoogleSignInResult(
                                result
                            )
                        )
                    },
                ) {
                    GoogleSignInButton(
                        modifier = Modifier.fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.onSurface,
                                RoundedCornerShape(8.dp)
                            )
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        mode = if (isSystemInDarkTheme()) GoogleButtonMode.Dark else GoogleButtonMode.Light,
                        text = "Continue With Google",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    ) { this.onClick() }
                }

                Spacer(Modifier.height(32.dp))

                AppleSignInButton(
                    modifier = Modifier.fillMaxWidth()
                        .height(48.dp),
                    onClick = {},
                    text = "Continue With apple ",
                    shape = RoundedCornerShape(8.dp),
                    fontFamily = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp
                    ).fontFamily!!,
                    mode = if (isSystemInDarkTheme()) AppleButtonMode.White else AppleButtonMode.Black,
                )
            }

            // Sign In Text
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Already have an account? ",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        text = "Sign In",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.clickable(
                            onClick = { authViewModel.onAction(UserAction.NavigateToSignIn) }
                        )
                    )
                }
            }
        }
    }

}
