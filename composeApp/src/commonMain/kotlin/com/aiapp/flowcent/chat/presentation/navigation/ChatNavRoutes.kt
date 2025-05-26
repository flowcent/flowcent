package com.aiapp.flowcent.chat.presentation.navigation

sealed class ChatNavRoutes(val route: String) {
    data object ChatScreen : ChatNavRoutes("chat_screen")
}