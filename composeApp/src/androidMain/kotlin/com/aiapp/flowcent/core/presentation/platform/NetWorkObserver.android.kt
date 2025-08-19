package com.aiapp.flowcent.core.presentation.platform

// in src/androidMain/kotlin/your/package/NetWorkObserver.android.kt

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

actual class NetWorkObserver(
    private val context: Context
) : ConnectivityObserver {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    actual override fun observe(): Flow<ConnectivityObserver.Status> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(ConnectivityObserver.Status.Available)
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                trySend(ConnectivityObserver.Status.Losing)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(ConnectivityObserver.Status.Lost)
            }

            override fun onUnavailable() {
                super.onUnavailable()
                trySend(ConnectivityObserver.Status.Unavailable)
            }
        }

        // Immediately check current status
        val currentNetwork = connectivityManager.activeNetwork
        if (currentNetwork == null) {
            trySend(ConnectivityObserver.Status.Unavailable)
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.conflate() // Use conflate to only emit the latest status

    actual override fun isMobileDataEnabled(): Flow<Boolean> = callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onCapabilitiesChanged(network: Network, caps: NetworkCapabilities) {
                super.onCapabilitiesChanged(network, caps)
                val isMobile = caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                trySend(isMobile)
            }

            override fun onLost(network: Network) {
                // When network is lost, mobile data is effectively disabled
                trySend(false)
            }
        }

        // Immediately check current status
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        val isCurrentlyMobile =
            capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
        trySend(isCurrentlyMobile)

        connectivityManager.registerDefaultNetworkCallback(callback)
        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.conflate()
}


@Composable
actual fun rememberConnectivityObserver(): ConnectivityObserver {
    val context = LocalContext.current
    return remember {
        NetWorkObserver(context)
    }
}