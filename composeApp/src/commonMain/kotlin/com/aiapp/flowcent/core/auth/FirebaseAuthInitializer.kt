package com.aiapp.flowcent.core.auth

import com.aiapp.flowcent.core.platform.onApplicationStartPlatformSpecific
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider


object FirebaseAuthInitializer {
    fun onApplicationStart() {
        onApplicationStartPlatformSpecific()
        GoogleAuthProvider.create(
            credentials = GoogleAuthCredentials(
                serverId = "161046411397-71cjns5cfjge3inb8gu9rep0b0nc2d8e.apps.googleusercontent.com"
            )
        )
    }
}