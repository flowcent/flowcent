package com.aiapp.flowcent.core.platform

import dev.gitlive.firebase.auth.PhoneVerificationProvider

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun onApplicationStartPlatformSpecific()

expect fun phoneVerificationProvider(): PhoneVerificationProvider
