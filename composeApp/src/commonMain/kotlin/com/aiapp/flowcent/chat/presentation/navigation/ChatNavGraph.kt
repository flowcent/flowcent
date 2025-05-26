package com.aiapp.flowcent.chat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.screen.ChatScreen
import com.aiapp.flowcent.core.navigation.addAnimatedComposable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatNavGraph(startDestination: ChatNavRoutes) {
    val localNavController = rememberNavController()
    val viewModel = koinViewModel<ChatViewModel>()
    val chatState by viewModel.chatState.collectAsState()

    NavHost(
        navController = localNavController,
        startDestination = startDestination.route
    ) {
        addAnimatedComposable(route = ChatNavRoutes.ChatScreen.route) {
            ChatScreen(chatState = chatState, viewModel = viewModel)
        }
    }
}