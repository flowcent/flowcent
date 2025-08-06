package com.aiapp.flowcent.core.presentation.platform

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun onApplicationStartPlatformSpecific()