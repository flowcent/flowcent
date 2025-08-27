/*
 * Created by Saeedus Salehin on 26/11/24, 10:36 PM.
 */

package com.aiapp.flowcent.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.aiapp.flowcent.accounts.presentation.navigation.addAccountsGraph
import com.aiapp.flowcent.auth.presentation.navigation.addAuthGraph
import com.aiapp.flowcent.chat.presentation.navigation.addChatGraph
import com.aiapp.flowcent.core.presentation.platform.SpeechRecognizer
import com.aiapp.flowcent.home.presentation.navigation.addHomeGraph
import com.aiapp.flowcent.splash.navigation.addSplashGraph
import dev.jordond.connectivity.Connectivity
import kotlinx.coroutines.launch

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: AppNavRoutes = AppNavRoutes.Splash,
    speechRecognizer: SpeechRecognizer
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        addSplashGraph(navController, modifier)
        addAuthGraph(navController, modifier)
        addAccountsGraph(navController, modifier)
        addHomeGraph(navController, modifier)
        addChatGraph(navController, modifier, speechRecognizer)
    }
}