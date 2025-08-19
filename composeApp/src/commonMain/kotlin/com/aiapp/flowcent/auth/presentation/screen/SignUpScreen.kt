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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.core.presentation.platform.ConnectivityObserver
import com.aiapp.flowcent.core.presentation.platform.rememberConnectivityObserver
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
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

    val connectivityObserver = rememberConnectivityObserver()

    // Observe general connectivity
    val status by connectivityObserver.observe()
        .collectAsState(initial = ConnectivityObserver.Status.Initializing)

    // Observe mobile data status
    val isMobileData by connectivityObserver.isMobileDataEnabled()
        .collectAsState(initial = false)

    LaunchedEffect(key1 = status) {
        authViewModel.onAction(UserAction.CheckInternet(status = status))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
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
                    text = "Sign Up",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
                    )
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Just a few quick things to get started",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
                )

                Spacer(Modifier.height(48.dp))

                // Email Field
                Text(text = "Email ID", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Enter Email ID") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(Modifier.height(24.dp))

                // Password Field
                Text(text = "Password", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Enter Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        val icon =
                            if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(icon, contentDescription = null)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(Modifier.height(16.dp))

                // Confirm Password Field
                Text(text = "Confirm Password", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(6.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("Enter Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = {
                        val icon =
                            if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(icon, contentDescription = null)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                )

                Spacer(Modifier.height(16.dp))

                //Terms and condition
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF9C27B0))
                    )
                    Text("I agree with the terms and condition")
                }

                Spacer(Modifier.height(16.dp))

                // Sign In Button
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0E0E0E))
                ) {
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White,
                    )
                }

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

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0E0E0E))
                ) {
                    Text(
                        text = "Continue With Apple",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                    )
                }
            }

            // Sign Up Text
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Already have an account? ")
                    Text(
                        text = "Sign In",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(
                            onClick = { authViewModel.onAction(UserAction.NavigateToSignIn) }
                        )
                    )
                }
            }
        }
    }

}
