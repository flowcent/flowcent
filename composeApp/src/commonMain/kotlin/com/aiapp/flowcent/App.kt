package com.aiapp.flowcent

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.core.navigation.AppNavGraph
import com.aiapp.flowcent.core.navigation.AppNavRoutes
import com.aiapp.flowcent.core.navigation.presentation.components.BottomNavigationBar
import com.aiapp.flowcent.core.navigation.presentation.model.NavItem
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navController = rememberNavController()

    val navItems = listOf(
        NavItem("Transaction", Res.drawable.compose_multiplatform, AppNavRoutes.Transaction.route),
        NavItem("Accounts", Res.drawable.compose_multiplatform, AppNavRoutes.Accounts.route),
        NavItem("Reflect", Res.drawable.compose_multiplatform, AppNavRoutes.Reflect.route)
    )

    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route

    val selectedIndex = navItems.indexOfFirst { it.route == currentRoute }.coerceAtLeast(0)

    MaterialTheme {
        Scaffold(
            bottomBar = {
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
        ) { padding ->
            AppNavGraph(navController = navController, modifier = Modifier.padding(padding))
        }
    }
}