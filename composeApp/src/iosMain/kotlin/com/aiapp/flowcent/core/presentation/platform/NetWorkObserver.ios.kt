package com.aiapp.flowcent.core.presentation.platform

// in src/iosMain/kotlin/your/package/ConnectivityObserver.kt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import platform.Network.NWPathMonitor
import platform.Network.NWPath
import platform.darwin.dispatch_get_main_queue

actual class NetWorkObserver : ConnectivityObserver {
    private val monitor = NWPathMonitor()

    override fun observe(): Flow<ConnectivityObserver.Status> = callbackFlow {
        val statusHandler = { path: NWPath ->
            if (path.status == platform.Network.NW_PATH_STATUS_SATISFIED) {
                trySend(ConnectivityObserver.Status.Available)
            } else {
                trySend(ConnectivityObserver.Status.Unavailable)
            }
        }

        monitor.pathUpdateHandler = statusHandler
        monitor.start(dispatch_get_main_queue())

        awaitClose {
            monitor.cancel()
        }
    }.conflate()

    override fun isMobileDataEnabled(): Flow<Boolean> = callbackFlow {
        val dataHandler = { path: NWPath ->
            val isMobile = path.usesInterfaceType(platform.Network.NW_INTERFACE_TYPE_CELLULAR)
            trySend(isMobile)
        }

        monitor.pathUpdateHandler = dataHandler
        monitor.start(dispatch_get_main_queue())

        awaitClose {
            monitor.cancel()
        }
    }.conflate()
}

@Composable
actual fun rememberConnectivityObserver(): ConnectivityObserver {
    return remember { NetWorkObserver() }
}