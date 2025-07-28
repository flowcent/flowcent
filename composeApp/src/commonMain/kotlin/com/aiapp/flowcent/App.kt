package com.aiapp.flowcent

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
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
import com.aiapp.flowcent.core.domain.model.NavItem
import com.aiapp.flowcent.core.platform.SpeechRecognizer
import com.aiapp.flowcent.core.presentation.navigation.AppNavGraph
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.BottomNavigationBar
import com.aiapp.flowcent.core.presentation.ui.theme.AppTheme
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.ic_chat
import flowcent.composeapp.generated.resources.ic_home
import flowcent.composeapp.generated.resources.ic_share_accounts
import kotlinx.coroutines.flow.collectLatest
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    speechRecognizer: SpeechRecognizer,
    prefs: DataStore<Preferences>
) {
    val globalNavController = rememberNavController()

    val navItems = listOf(
        NavItem("Home", Res.drawable.ic_home, AppNavRoutes.Home.route),
        NavItem("Chat", Res.drawable.ic_chat, AppNavRoutes.Chat.route),
        NavItem("Accounts", Res.drawable.ic_share_accounts, AppNavRoutes.Accounts.route)
    )

    val currentDestination by globalNavController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    val selectedIndex = navItems.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

    val shouldNotShowBottomNavs = currentRoute == AppNavRoutes.Auth.route ||
            currentRoute == AppNavRoutes.Profile.route

//    var hasCurrentUser by remember { mutableStateOf(false) }
//
//
//    LaunchedEffect(key1 = Unit) {
//        Firebase.auth.authStateChanged.collectLatest {
//            hasCurrentUser = it?.uid != null
//        }
//    }
    val hasCurrentUser = Firebase.auth.currentUser != null


    AppTheme {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
            bottomBar = {
                if (shouldNotShowBottomNavs.not()) {
                    BottomNavigationBar(
                        items = navItems,
                        selectedIndex = selectedIndex,
                        onItemSelected = { index ->
                            val route = navItems[index].route
                            if (currentRoute != route) {
                                globalNavController.navigate(route) {
                                    popUpTo(globalNavController.graph.startDestinationId) {
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
                globalNavController = globalNavController,
                modifier = Modifier.padding(innerPadding),
                speechRecognizer = speechRecognizer,
                startDestination = if (hasCurrentUser) AppNavRoutes.Home else AppNavRoutes.Auth
            )
        }
    }
}
