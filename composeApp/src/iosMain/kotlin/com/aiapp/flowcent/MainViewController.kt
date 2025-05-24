package com.aiapp.flowcent

import androidx.compose.ui.window.ComposeUIViewController
import com.aiapp.flowcent.di.initKoin

fun MainViewController() = ComposeUIViewController(configure = { initKoin() }) {
    App()
}