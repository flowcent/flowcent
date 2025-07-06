package com.aiapp.flowcent.home.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aiapp.flowcent.home.presentation.HomeState
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.google.GoogleButtonMode
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import dev.gitlive.firebase.auth.FirebaseUser

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    homeState: HomeState,
    navController: NavController
) {
    var user: FirebaseUser? = null
    val onFirebaseResult: (Result<FirebaseUser?>) -> Unit = { result ->
        if (result.isSuccess) {
            val firebaseUser = result.getOrNull()
            user = firebaseUser
            println("Sohan onFirebaseResult firebaseUser: ${firebaseUser?.uid}")
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
            onResult = onFirebaseResult,
        ) {
            GoogleSignInButton(
                modifier = Modifier.fillMaxWidth(),
                mode = GoogleButtonMode.Dark,
                fontSize = MaterialTheme.typography.button.fontSize,

                ) { this.onClick() }
        }

//        OutlinedButton(
//            onClick = { homeViewModel.onAction(UserAction.FirebaseSignOut) }
//
//        ) {
//            Text("Google sign out")
//        }
    }
}
