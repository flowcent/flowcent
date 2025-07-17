package com.aiapp.flowcent

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.core.domain.model.NavItem
import com.aiapp.flowcent.core.platform.SpeechRecognizer
import com.aiapp.flowcent.core.presentation.navigation.BottomNavigationBar
import com.aiapp.flowcent.core.presentation.navigation.AppNavGraph
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.ui.theme.AppTheme
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(
    speechRecognizer: SpeechRecognizer,
    prefs: DataStore<Preferences>
) {
    val globalNavController = rememberNavController()

    val navItems = listOf(
        NavItem("Home", Res.drawable.compose_multiplatform, AppNavRoutes.Home.route),
        NavItem("Chat", Res.drawable.compose_multiplatform, AppNavRoutes.Chat.route),
        NavItem("Accounts", Res.drawable.compose_multiplatform, AppNavRoutes.Accounts.route)
    )

    val currentDestination by globalNavController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    val selectedIndex = navItems.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

    AppTheme {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.windowInsetsPadding(WindowInsets.systemBars),
            bottomBar = {
                if (currentRoute != AppNavRoutes.Auth.route) {
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
                startDestination = AppNavRoutes.Home
            )
        }
    }
}
