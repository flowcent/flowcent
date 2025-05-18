package com.aiapp.flowcent

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.aiapp.flowcent.core.navigation.AppNavGraph
import com.aiapp.flowcent.core.navigation.AppNavRoutes
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.compose_multiplatform
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.aiapp.flowcent.core.navigation.presentation.components.BottomNavigationBar
import com.aiapp.flowcent.core.navigation.presentation.model.NavItem

@Composable
@Preview
fun App() {
    val navItems = listOf(
        NavItem("Accounts", Res.drawable.compose_multiplatform),
        NavItem("Transaction", Res.drawable.compose_multiplatform),
        NavItem("Reflect", Res.drawable.compose_multiplatform)
    )

    var selectedIndex by remember { mutableStateOf(0) }

    MaterialTheme {
        Scaffold(
            bottomBar = {
                BottomNavigationBar(
                    items = navItems,
                    selectedIndex = selectedIndex,
                    onItemSelected = { selectedIndex = it })
            }) {
            AppNavGraph(
                startDestination = when (selectedIndex) {
                    0 -> AppNavRoutes.Accounts
                    1 -> AppNavRoutes.Transaction
                    2 -> AppNavRoutes.Reflect
                    else -> AppNavRoutes.Transaction
                }
            )
        }
    }
}