package com.aiapp.flowcent.auth.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UiEvent
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.core.presentation.components.AppButton
import com.aiapp.flowcent.core.presentation.components.AppTextField
import com.aiapp.flowcent.core.presentation.components.NoInternet
import com.aiapp.flowcent.core.presentation.platform.ConnectivityObserver
import com.aiapp.flowcent.core.presentation.platform.rememberConnectivityObserver
import com.aiapp.flowcent.core.utils.DialogType
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.google.GoogleButtonMode
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    authState: AuthState,
) {


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
//            .drawWithCache {
//                onDrawBehind {
//                    drawRect(
//                        brush = Brush.verticalGradient(
//                            colors = listOf(
//                                Color(0xFFECE8FD),
//                                Color(0xFFFFFFFF),
//                                Color(0xFFEBF9FE),
//                                Color(0xFFF9E8FA),
//                            )
//                        )
//                    )
//                }
//            }
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(Modifier.height(72.dp))

                Text(
                    text = "Sign In", style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold, fontSize = 32.sp
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Welcome back you’ve been missed",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )

                Spacer(Modifier.height(48.dp))

                // Email Field
                Text(text = "Email ID", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                AppTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Enter Email ID",
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(24.dp))

                // Password Field
                Text(text = "Password", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))

                AppTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Enter password",
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val icon =
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(icon, contentDescription = null)
                        }
                    })

                Spacer(Modifier.height(16.dp))

                // Remember Me and Forgot Password
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = rememberMe,
                            onCheckedChange = { rememberMe = it },
                            colors = CheckboxDefaults.colors(checkedColor = Color(0xFF9C27B0))
                        )
                        Text("Remember Me")
                    }
                    TextButton(onClick = {}) {
                        Text("Forgot Password?", color = Color.Black)
                    }
                }

                Spacer(Modifier.height(24.dp))

                // Sign In Button
                AppButton(
                    onClick = {
                        authViewModel.onAction(
                            UserAction.SignInWithEmailPass(
                                email, password
                            )
                        )
                    },
                    text = "Sign In",
                    backgroundColor = Color(0xFF0E0E0E),
                    isLoading = authState.isEmailSignInProcessing
                )

                Spacer(Modifier.height(24.dp))

                // Or with Divider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Divider(modifier = Modifier.weight(1f))
                    Text("  Or with  ", color = Color.Gray)
                    Divider(modifier = Modifier.weight(1f))
                }

                Spacer(Modifier.height(24.dp))

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
                            .background(Color(0xFF0E0E0E), shape = RoundedCornerShape(8.dp))
                            .height(60.dp),
                        shape = RoundedCornerShape(8.dp),
                        mode = GoogleButtonMode.Light,
                        text = "Continue With Google",
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    ) { this.onClick() }
                }

                Spacer(Modifier.height(16.dp))

                AppButton(
                    onClick = { /* Handle click */ },
                    text = "Continue With Apple",
                    backgroundColor = Color(0xFF0E0E0E),
                )
            }

            // Sign Up Text
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Don’t have an account? ")
                    Text(
                        text = "Sign Up",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(
                            onClick = { authViewModel.onAction(UserAction.NavigateToSignUp) })
                    )
                }
            }
        }
    }

}
