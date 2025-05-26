/*
 * Created by Saeedus Salehin on 15/5/25, 2:50 PM.
 */

package com.aiapp.flowcent.home.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.navigation.ChatNavGraph
import com.aiapp.flowcent.chat.presentation.navigation.ChatNavRoutes
import com.aiapp.flowcent.chat.presentation.screen.ChatScreen
import com.aiapp.flowcent.core.navigation.addAnimatedComposable
import com.aiapp.flowcent.home.presentation.HomeViewModel
import com.aiapp.flowcent.home.presentation.screens.HomeScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeNavGraph(startDestination: HomeNavRoutes) {
    val localNavController = rememberNavController()
    val viewModel = koinViewModel<HomeViewModel>()
    val state by viewModel.state.collectAsState()

    val chatViewModel = koinViewModel<ChatViewModel>()
    val chatState by chatViewModel.chatState.collectAsState()

    NavHost(
        navController = localNavController,
        startDestination = startDestination.route
    ) {
        addAnimatedComposable(route = HomeNavRoutes.HomeScreen.route) {
            HomeScreen(navController = localNavController)
        }
        addAnimatedComposable(route = ChatNavRoutes.ChatScreen.route) {
            ChatScreen(chatState = chatState, viewModel = chatViewModel)
        }
    }
}