package com.aiapp.flowcent.voice

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer as AndroidSpeechRecognizer
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale

actual class SpeechRecognizer(private val context: Context) {
    private var androidSpeechRecognizer: AndroidSpeechRecognizer? = null
    private val recognizerIntent: Intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toLanguageTag())
        putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) // Get partial results
    }

    actual fun isRecognitionAvailable(): Boolean {
        return AndroidSpeechRecognizer.isRecognitionAvailable(context)
    }

    actual fun startListening(): Flow<String> = callbackFlow {
        if (!isRecognitionAvailable()) {
            send("Speech recognition not available") // Or emit an error state
            close()
            return@callbackFlow
        }

        androidSpeechRecognizer = AndroidSpeechRecognizer.createSpeechRecognizer(context)
        androidSpeechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                val errorMessage = when (error) {
                    AndroidSpeechRecognizer.ERROR_AUDIO -> "Audio recording error."
                    AndroidSpeechRecognizer.ERROR_CLIENT -> "Client side error."
                    AndroidSpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions."
                    AndroidSpeechRecognizer.ERROR_NETWORK -> "Network error."
                    AndroidSpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout."
                    AndroidSpeechRecognizer.ERROR_NO_MATCH -> "No speech recognized."
                    AndroidSpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy."
                    AndroidSpeechRecognizer.ERROR_SERVER -> "Server error."
                    AndroidSpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input."
                    else -> "Unknown error ($error)."
                }
                trySend("Error: $errorMessage") // Emit error as string for now
                // Close the flow gracefully, without throwing an uncaught exception
                // This means the flow collection will just stop.
                // If you want the collection to continue for a new attempt, you'd manage lifecycle differently.
                close()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(AndroidSpeechRecognizer.RESULTS_RECOGNITION)
                val bestResult = matches?.getOrNull(0)
                if (bestResult != null) {
                    trySend(bestResult)
                }
                close() // Close flow after final result for one-shot recognition
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(AndroidSpeechRecognizer.RESULTS_RECOGNITION)
                val partialText = matches?.getOrNull(0)
                if (partialText != null) {
                    trySend(partialText) // Send partial results
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        androidSpeechRecognizer?.startListening(recognizerIntent)

        awaitClose {
            androidSpeechRecognizer?.destroy()
            androidSpeechRecognizer = null
        }
    }

    actual fun stopListening() {
        androidSpeechRecognizer?.stopListening()
    }

    actual fun cancel() {
        androidSpeechRecognizer?.cancel()
    }
}