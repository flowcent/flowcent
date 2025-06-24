package com.aiapp.flowcent.chat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.screen.ChatScreen
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import com.aiapp.flowcent.core.platform.SpeechRecognizer
import com.aiapp.flowcent.core.presentation.PermissionsViewModel
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatNavGraph(startDestination: ChatNavRoutes, speechRecognizer: SpeechRecognizer) {
    val localNavController = rememberNavController()
    val viewModel = koinViewModel<ChatViewModel>()
    val chatState by viewModel.chatState.collectAsState()

    val factory = rememberPermissionsControllerFactory()
    val controller = remember(factory) {
        factory.createPermissionsController()
    }

    BindEffect(controller)

    val permissionVM = androidx.lifecycle.viewmodel.compose.viewModel {
        PermissionsViewModel(controller)
    }

    NavHost(
        navController = localNavController,
        startDestination = startDestination.route
    ) {
        addAnimatedComposable(route = ChatNavRoutes.ChatScreen.route) {
            ChatScreen(
                chatState = chatState,
                viewModel = viewModel,
                speechRecognizer = speechRecognizer,
                permissionsVM = permissionVM
            )
        }
    }
}