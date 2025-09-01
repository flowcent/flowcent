package com.aiapp.flowcent.auth.presentation.event

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * A lifecycle-aware Composable that collects events from a Flow
 * and executes a callback for each event.
 *
 * This is the recommended way to collect from a Flow in Compose, as it
 * automatically cancels and restarts collection when the screen goes
 * to the background and comes back.
 *
 * @param T The type of the event.
 * @param eventFlow The Flow of events to be collected.
 * @param onEvent The callback to be executed for each event.
 */
@Composable
fun <T> EventHandler(
    eventFlow: Flow<T>,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(eventFlow, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            eventFlow.collect { event ->
                onEvent(event)
            }
        }
    }
}