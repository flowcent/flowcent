package com.aiapp.flowcent.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.auth.presentation.components.Events
import com.aiapp.flowcent.util.Constants
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.google.GoogleButtonMode
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import dev.gitlive.firebase.auth.FirebaseUser

@Composable
fun SignInScreen(
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

    val onGoogleSignInResult: (Result<FirebaseUser?>) -> Unit = { result ->
        if (result.isSuccess) {
            val firebaseUser = result.getOrNull()
            if (firebaseUser != null) {
                authViewModel.onAction(
                    UserAction.IsUserExist(firebaseUser, Constants.SIGN_IN_TYPE_GOOGLE)
                )

                authViewModel.onAction(UserAction.SaveUserUid(firebaseUser.uid))
            }

        } else {
            println("Sohan onFirebaseResult Error Result: ${result.exceptionOrNull()?.message}")
        }

    }

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Auth Screen")

        GoogleButtonUiContainerFirebase(
            linkAccount = false,
            filterByAuthorizedAccounts = false,
            onResult = onGoogleSignInResult,
        ) {
            GoogleSignInButton(
                modifier = Modifier.fillMaxWidth(),
                mode = GoogleButtonMode.Dark,
                fontSize = MaterialTheme.typography.button.fontSize,

                ) { this.onClick() }
        }

        OutlinedButton(
            onClick = { authViewModel.onAction(UserAction.FirebaseSignOut) }

        ) {
            Text("Google sign out")
        }
    }
}
