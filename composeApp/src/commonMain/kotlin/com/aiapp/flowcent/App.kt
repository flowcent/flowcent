package com.aiapp.flowcent

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.auth.presentation.navigation.AuthNavRoutes
import com.aiapp.flowcent.core.domain.model.NavItem
import com.aiapp.flowcent.core.presentation.navigation.AppNavGraph
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.BottomNavigationBar
import com.aiapp.flowcent.core.presentation.navigation.GetTopBarForRoute
import com.aiapp.flowcent.core.presentation.platform.SpeechRecognizer
import com.aiapp.flowcent.core.presentation.ui.theme.AppTheme
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.ic_chat
import flowcent.composeapp.generated.resources.ic_chat_selected
import flowcent.composeapp.generated.resources.ic_home
import flowcent.composeapp.generated.resources.ic_home_selected
import flowcent.composeapp.generated.resources.ic_share_accounts
import flowcent.composeapp.generated.resources.ic_share_accounts_selected
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    speechRecognizer: SpeechRecognizer,
    prefs: DataStore<Preferences>
) {
    val navController = rememberNavController()

    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    var showBottomNav by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = currentRoute) {
        if (currentRoute == AppNavRoutes.Splash.route ||
            currentRoute == AppNavRoutes.Auth.route ||
            currentRoute == AuthNavRoutes.ProfileScreen.route ||
            currentRoute == AuthNavRoutes.BasicIntroScreen.route ||
            currentRoute == AuthNavRoutes.SignInScreen.route ||
            currentRoute == AuthNavRoutes.SignUpScreen.route
        ) {
            showBottomNav = false
        } else {
            delay(300)
            showBottomNav = true
        }
    }


    val navItems = listOf(
        NavItem(
            "Home",
            Res.drawable.ic_home,
            Res.drawable.ic_home_selected,
            AppNavRoutes.Home.route
        ),
        NavItem(
            "Chat",
            Res.drawable.ic_chat,
            Res.drawable.ic_chat_selected,
            AppNavRoutes.Chat.route
        ),
        NavItem(
            "Shared",
            Res.drawable.ic_share_accounts,
            Res.drawable.ic_share_accounts_selected,
            AppNavRoutes.Accounts.route
        )
    )


    val selectedIndex = navItems.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

    AppTheme {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                GetTopBarForRoute(
                    navController = navController,
                    route = currentRoute.orEmpty()
                )
            },
            bottomBar = {
                if (showBottomNav) {
                    BottomNavigationBar(
                        items = navItems,
                        selectedIndex = selectedIndex,
                        onItemSelected = { index ->
                            val route = navItems[index].route
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            AppNavGraph(
                modifier = Modifier.padding(innerPadding),
                navController = navController,
                speechRecognizer = speechRecognizer,
                startDestination = AppNavRoutes.Splash
            )
        }
    }
}