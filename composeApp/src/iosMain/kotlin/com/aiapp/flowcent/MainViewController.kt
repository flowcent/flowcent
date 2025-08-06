package com.aiapp.flowcent

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.aiapp.flowcent.core.data.local.datastore.createDataStore
import com.aiapp.flowcent.di.initKoin
import com.aiapp.flowcent.core.presentation.platform.SpeechRecognizer

fun MainViewController() = ComposeUIViewController(configure = { initKoin() }) {
    val speechRecognizer = SpeechRecognizer()
    App(
        speechRecognizer = speechRecognizer,
        prefs = remember {
            createDataStore()
        }
    )
}