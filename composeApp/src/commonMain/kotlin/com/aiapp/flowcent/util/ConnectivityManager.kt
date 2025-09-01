package com.aiapp.flowcent.util

import dev.jordond.connectivity.Connectivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object ConnectivityManager {
    private val connectivity = Connectivity() // The Jordon/Connectivity library instance
    private val _connectivityStatus = MutableStateFlow(true)
    val connectivityStatus: StateFlow<Boolean> = _connectivityStatus.asStateFlow()

    init {
        // Start the connectivity check as soon as the object is created
        connectivity.start()

        // Use a CoroutineScope to launch a job that collects status updates
        // This scope must have a lifecycle that matches the application's
        // For a singleton, this is typically fine.
        val managerScope = CoroutineScope(Dispatchers.Default)
        managerScope.launch {
            connectivity.statusUpdates.collect { status ->
                _connectivityStatus.value = when (status) {
                    is Connectivity.Status.Connected -> true
                    is Connectivity.Status.Disconnected -> false
                }
            }
        }
    }
}