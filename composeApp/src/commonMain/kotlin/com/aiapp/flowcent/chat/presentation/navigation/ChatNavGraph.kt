package com.aiapp.flowcent.chat.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.aiapp.flowcent.chat.presentation.ChatViewModel
import com.aiapp.flowcent.chat.presentation.screen.BaseScreen
import com.aiapp.flowcent.chat.presentation.screen.ChatListScreen
import com.aiapp.flowcent.chat.presentation.screen.VoiceAssistantScreen
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import com.aiapp.flowcent.core.presentation.permission.PermissionsViewModel
import com.aiapp.flowcent.core.presentation.platform.SpeechRecognizer
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.addChatGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    speechRecognizer: SpeechRecognizer
) {
    navigation(
        route = AppNavRoutes.Chat.route,
        startDestination = ChatNavRoutes.ChatListScreen.route
    ) {
        addAnimatedComposable(route = ChatNavRoutes.ChatListScreen.route) { navBackStackEntry ->
            val chatNavGraphEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppNavRoutes.Chat.route)
            }
            val viewModel = koinViewModel<ChatViewModel>(
                viewModelStoreOwner = chatNavGraphEntry
            )

            val factory = rememberPermissionsControllerFactory()
            val controller = remember(factory) {
                factory.createPermissionsController()
            }

            BindEffect(controller)

            val permissionVM = viewModel {
                PermissionsViewModel(controller)
            }

            val fcPermissionsState by permissionVM.state.collectAsState()

            BaseScreen(
                navController = navController,
                viewModel = viewModel,
                modifier = modifier,
                speechRecognizer = speechRecognizer,
                permissionsVM = permissionVM,
                fcPermissionsState = fcPermissionsState,
            ) { modifier, chatViewModel, chatState ->
                ChatListScreen(
                    modifier = modifier,
                    chatState = chatState,
                    viewModel = chatViewModel,
                )
            }
        }

        addAnimatedComposable(route = ChatNavRoutes.VoiceAssistantScreen.route) { navBackStackEntry ->
            val chatNavGraphEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppNavRoutes.Chat.route)
            }
            val viewModel = koinViewModel<ChatViewModel>(
                viewModelStoreOwner = chatNavGraphEntry
            )

            val factory = rememberPermissionsControllerFactory()
            val controller = remember(factory) {
                factory.createPermissionsController()
            }

            BindEffect(controller)

            val permissionVM = viewModel {
                PermissionsViewModel(controller)
            }

            val fcPermissionsState by permissionVM.state.collectAsState()

            BaseScreen(
                navController = navController,
                viewModel = viewModel,
                modifier = modifier,
                speechRecognizer = speechRecognizer,
                permissionsVM = permissionVM,
                fcPermissionsState = fcPermissionsState,
            ) { modifier, chatViewModel, chatState ->
                VoiceAssistantScreen(
                    modifier = modifier,
                    chatState = chatState,
                    viewModel = chatViewModel,
                )
            }
        }
    }
}