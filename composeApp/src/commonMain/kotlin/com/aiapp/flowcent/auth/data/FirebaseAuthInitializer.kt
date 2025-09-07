package com.aiapp.flowcent.auth.data

import com.aiapp.flowcent.auth.util.GOOGLE_SIGN_IN_SERVER_ID_DEBUG
import com.aiapp.flowcent.auth.util.GOOGLE_SIGN_IN_SERVER_ID_PROD
import com.aiapp.flowcent.core.presentation.platform.getPlatform
import com.aiapp.flowcent.core.presentation.platform.onApplicationStartPlatformSpecific
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import io.github.aakira.napier.Napier

object FirebaseAuthInitializer {
    fun onApplicationStart() {
        onApplicationStartPlatformSpecific()
        val platform = getPlatform()
        val serverId =
            if (platform.isDebug) GOOGLE_SIGN_IN_SERVER_ID_DEBUG else GOOGLE_SIGN_IN_SERVER_ID_PROD

        GoogleAuthProvider.Companion.create(
            credentials = GoogleAuthCredentials(
                serverId = serverId
            )
        )
    }
}