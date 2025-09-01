package com.aiapp.flowcent.core.presentation.platform

interface Platform {
    val name: String
    val isDebug: Boolean
}

expect fun getPlatform(): Platform

expect fun onApplicationStartPlatformSpecific()

expect fun getRevenueCatApiKey(): String


