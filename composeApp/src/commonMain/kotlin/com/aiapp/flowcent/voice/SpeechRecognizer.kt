package com.aiapp.flowcent.voice

import kotlinx.coroutines.flow.Flow

expect class SpeechRecognizer {
    fun isRecognitionAvailable(): Boolean
    fun startListening(): Flow<String> // Emits recognized text
    fun stopListening()
    fun cancel() // Stop listening and discard current recognition
}

// You might also want to define a sealed class for errors or states
sealed class SpeechRecognizerState {
    object Idle : SpeechRecognizerState()
    object Listening : SpeechRecognizerState()
    data class Result(val text: String) : SpeechRecognizerState()
    data class Error(val message: String) : SpeechRecognizerState()
    object NotAvailable : SpeechRecognizerState()
}