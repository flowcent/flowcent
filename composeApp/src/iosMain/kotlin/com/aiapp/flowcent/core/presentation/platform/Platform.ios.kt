package com.aiapp.flowcent.core.presentation.platform

import platform.UIKit.UIDevice

class IOSPlatform : Platform {
    override val name: String =
        UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override val isDebug: Boolean = true
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun onApplicationStartPlatformSpecific() {
}