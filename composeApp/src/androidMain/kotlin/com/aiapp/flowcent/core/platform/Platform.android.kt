package com.aiapp.flowcent.core.platform

import android.os.Build
import com.aiapp.flowcent.MainActivity
import dev.gitlive.firebase.auth.PhoneVerificationProvider

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun onApplicationStartPlatformSpecific() {

}

actual fun phoneVerificationProvider(): PhoneVerificationProvider {
    return AndroidPhoneVerificationProvider(activity = MainActivity())
}
