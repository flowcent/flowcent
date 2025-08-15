package com.aiapp.flowcent.chat.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.screen.ChatScreen
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import com.aiapp.flowcent.core.presentation.platform.SpeechRecognizer
import com.aiapp.flowcent.core.presentation.permission.PermissionsViewModel
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.addChatGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    speechRecognizer: SpeechRecognizer
) {
    addAnimatedComposable(route = AppNavRoutes.Chat.route) {
        val viewModel = koinViewModel<ChatViewModel>()
        val chatState by viewModel.chatState.collectAsState()

        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

        BindEffect(controller)

        val permissionVM = viewModel {
            PermissionsViewModel(controller)
        }

        val fcPermissionsState by permissionVM.state.collectAsState()

        ChatScreen(
            modifier = modifier,
            chatState = chatState,
            viewModel = viewModel,
            speechRecognizer = speechRecognizer,
            permissionsVM = permissionVM,
            fcPermissionsState = fcPermissionsState
        )
    }
}