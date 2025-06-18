package com.aiapp.flowcent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.aiapp.flowcent.ai.FlowCentAi
import com.aiapp.flowcent.voice.SpeechRecognizer
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)

        setContent {
            val speechRecognizer = SpeechRecognizer(applicationContext)
            val flowCentAi = FlowCentAi()
            App(
                speechRecognizer = speechRecognizer,
            )
        }
    }
}