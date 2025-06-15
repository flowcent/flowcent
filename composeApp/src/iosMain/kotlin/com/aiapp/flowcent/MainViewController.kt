package com.aiapp.flowcent

import androidx.compose.ui.window.ComposeUIViewController
import com.aiapp.flowcent.di.initKoin
import com.aiapp.flowcent.voice.SpeechRecognizer

fun MainViewController() = ComposeUIViewController(configure = { initKoin() }) {
    val speechRecognizer = SpeechRecognizer()
    App(
        speechRecognizer = speechRecognizer
    )
}