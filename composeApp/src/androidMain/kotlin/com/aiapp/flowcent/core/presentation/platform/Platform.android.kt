package com.aiapp.flowcent.core.presentation.platform

import android.os.Build
import com.aiapp.flowcent.BuildConfig

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    override val isDebug: Boolean = BuildConfig.DEBUG
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun onApplicationStartPlatformSpecific() {}

actual fun getRevenueCatApiKey(): String = "goog_btMTTbPOzMyHggASyEHhELGRvzN"