package com.aiapp.flowcent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.aiapp.flowcent.voice.SpeechRecognizer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val speechRecognizer = SpeechRecognizer(applicationContext)
            App(
                speechRecognizer = speechRecognizer,
            )
        }
    }
}