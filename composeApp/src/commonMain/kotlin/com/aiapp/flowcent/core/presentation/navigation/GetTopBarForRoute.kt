/*
 * Created by Saeedus Salehin on 13/8/25, 12:10 PM.
 */

package com.aiapp.flowcent.core.presentation.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.aiapp.flowcent.accounts.presentation.navigation.AccountsNavRoutes
import com.aiapp.flowcent.auth.presentation.navigation.AuthNavRoutes
import com.aiapp.flowcent.chat.presentation.navigation.ChatNavRoutes
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.marcus
import flowcent.composeapp.generated.resources.marcusUpdate
import org.jetbrains.compose.resources.painterResource


//TODO: NEEDS REFACTORING HERE
@Composable
fun GetTopBarForRoute(navController: NavHostController, route: String) {
    when (route) {
        AppNavRoutes.Accounts.route -> AppBar(navController = navController, title = "Shared")
        AppNavRoutes.Auth.route -> null
        AppNavRoutes.Chat.route -> AppBar(navController = navController, title = "Chat")
        ChatNavRoutes.ChatListScreen.route -> AppBar(
            navController = navController,
            title = "Chat With Marcus",
            imagePainter = painterResource(Res.drawable.marcusUpdate)
        )

        AuthNavRoutes.ProfileScreen.route -> AppBar(
            navController = navController,
            title = "Profile"
        )

        AccountsNavRoutes.AccountsHomeScreen.route -> AppBar(
            navController = navController,
            title = "Accounts"
        )

        AccountsNavRoutes.AddAccountScreen.route -> AppBar(
            navController = navController,
            title = "Add Account"
        )

        AccountsNavRoutes.AccountDetailScreen.route -> AppBar(
            navController = navController,
            title = "Account Details"
        )

        AppNavRoutes.Home.route -> null
        AppNavRoutes.Profile.route -> null
        AppNavRoutes.Splash.route -> null
        else -> null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    navController: NavHostController,
    title: String,
    imagePainter: Painter? = null,
    showBackButton: Boolean = true,
    actions: @Composable (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (imagePainter != null) {
                        Image(
                            painter = imagePainter,
                            contentDescription = null,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.inverseSurface
                    )
                }
            },
            navigationIcon = {
                if (showBackButton) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.inverseSurface
                        )
                    }
                } else null
            },
            actions = { actions?.invoke() },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                navigationIconContentColor = MaterialTheme.colorScheme.inverseSurface,
                actionIconContentColor = MaterialTheme.colorScheme.inverseSurface
            )
        )

//        Divider(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.outlineVariant))
    }
}