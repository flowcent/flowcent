package com.aiapp.flowcent.chat.presentation.navigation

sealed class ChatNavRoutes(val route: String) {
    data object ChatListScreen : ChatNavRoutes("chat_list_screen")
//    data object VoiceAssistantScreen : ChatNavRoutes("voice_assistant_screen")
}