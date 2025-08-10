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
import androidx.compose.material.TextButton
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.auth.presentation.components.Events
import com.aiapp.flowcent.core.domain.utils.Constants
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.google.GoogleButtonMode
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import dev.gitlive.firebase.auth.FirebaseUser
import io.github.aakira.napier.Napier
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    authState: AuthState,
    localNavController: NavController,
    globalNavController: NavController
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMe by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val onGoogleSignInResult: (Result<FirebaseUser?>) -> Unit = { result ->
        try {
            if (result.isSuccess) {
                val firebaseUser = result.getOrNull()
                if (firebaseUser != null) {
                    authViewModel.onAction(
                        UserAction.IsUserExist(firebaseUser, Constants.SIGN_IN_TYPE_GOOGLE)
                    )

                    authViewModel.onAction(UserAction.SaveUserUid(firebaseUser.uid))
                }

            } else {
                if (result.exceptionOrNull() is CancellationException) {
                    Napier.e("Sohan Google Sign-In was cancelled by the user.")
                } else {
                    Napier.e("Sohan onFirebaseResult Error: ${result.exceptionOrNull()?.message}")
                }
            }
        } catch (ex: Exception) {
            Napier.e("Sohan onFirebaseResult Exception: ${ex.message}")
        }

    }


    Events(
        authViewModel = authViewModel,
        globalNavController = globalNavController,
        localNavController = localNavController
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFF5F5F5), Color(0xFFEAEAEA))
                )
            )
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
                    text = "Sign In",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp
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
                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0E0E0E))
                ) {
                    Text(
                        text = "Sign In",
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
                    onResult = onGoogleSignInResult,
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("Don’t have an account? ")
                    Text(
                        text = "Sign Up",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable(
                            onClick = { authViewModel.onAction(UserAction.NavigateToSignUp) }
                        )
                    )
                }
            }
        }
    }

}
