package com.aiapp.flowcent.core.presentation.platform

// In the same file or a new one in commonMain
import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.Flow

expect class NetWorkObserver : ConnectivityObserver {
    override fun observe(): Flow<ConnectivityObserver.Status>
    override fun isMobileDataEnabled(): Flow<Boolean>
}