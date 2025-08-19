package com.aiapp.flowcent.core.presentation.platform

import kotlinx.coroutines.flow.Flow

interface ConnectivityObserver {
    fun observe(): Flow<Status>

    // On mobile, it checks if the active connection is cellular.
    fun isMobileDataEnabled(): Flow<Boolean>

    enum class Status {
        Initializing, Available, Unavailable, Losing, Lost
    }
}