package com.aiapp.flowcent.core.platform

import com.aiapp.flowcent.core.presentation.platform.Platform
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun onApplicationStartPlatformSpecific() {
}