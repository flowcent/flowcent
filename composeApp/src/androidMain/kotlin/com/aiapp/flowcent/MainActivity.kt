package com.aiapp.flowcent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.aiapp.flowcent.core.data.local.datastore.createDataStore
import com.aiapp.flowcent.core.presentation.platform.SpeechRecognizer
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        FirebaseApp.initializeApp(this)

        setContent {
            val speechRecognizer = SpeechRecognizer(applicationContext)
            App(
                speechRecognizer = speechRecognizer,
                prefs = remember {
                    createDataStore(applicationContext)
                }
            )
        }
    }
}