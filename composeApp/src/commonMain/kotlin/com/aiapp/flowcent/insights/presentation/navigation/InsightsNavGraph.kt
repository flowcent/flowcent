/*
 * Created by Saeedus Salehin on 24/8/25, 5:40 pm.
 */

package com.aiapp.flowcent.insights.presentation.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.insights.presentation.screens.InsightsScreen

fun NavGraphBuilder.addInsightsGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    composable(route = AppNavRoutes.Insights.route) {
        InsightsScreen()
    }
}
