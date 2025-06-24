package com.aiapp.flowcent.core.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryRecord
import platform.AVFAudio.AVAudioSessionModeMeasurement
import platform.AVFAudio.setActive
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Speech.SFSpeechAudioBufferRecognitionRequest
import platform.Speech.SFSpeechRecognitionTask
import platform.Speech.SFSpeechRecognizer
import platform.Speech.SFSpeechRecognizerAuthorizationStatus

actual class SpeechRecognizer {

    private var speechRecognizer: SFSpeechRecognizer? = null
    private var recognitionRequest: SFSpeechAudioBufferRecognitionRequest? = null
    private var recognitionTask: SFSpeechRecognitionTask? = null
    private val audioEngine = platform.AVFAudio.AVAudioEngine()

    actual fun isRecognitionAvailable(): Boolean {
        return speechRecognizer?.isAvailable() ?: false
    }

    actual fun startListening(): Flow<String> = callbackFlow {
        if (!isRecognitionAvailable()) {
            send("Speech recognition not available")
            close()
            return@callbackFlow
        }

        // Request authorization
        SFSpeechRecognizer.requestAuthorization { status ->
            if (status == SFSpeechRecognizerAuthorizationStatus.SFSpeechRecognizerAuthorizationStatusAuthorized) {
                launch(Dispatchers.Main) { // Ensure UI updates on main thread
                    startAudioEngineAndRecognize(this@callbackFlow)
                }
            } else {
                trySend("Permission not granted: $status")
                close()
            }
        }

        awaitClose {
            cancel()
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun startAudioEngineAndRecognize(emitter: ProducerScope<String>) {
        try {
            // Configure audio session
            val audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(AVAudioSessionCategoryRecord, error = null)
            audioSession.setMode(AVAudioSessionModeMeasurement, error = null)
            audioSession.setActive(true, error = null)

            speechRecognizer =
                SFSpeechRecognizer(locale = NSLocale.currentLocale)
            recognitionRequest = SFSpeechAudioBufferRecognitionRequest()
            recognitionRequest?.setShouldReportPartialResults(true)

            val inputNode = audioEngine.inputNode
            val recordingFormat = inputNode.outputFormatForBus(0U)
            inputNode.installTapOnBus(
                bus = 0U,
                bufferSize = 1024U,
                format = recordingFormat
            ) { buffer, _ ->
                if (buffer != null) {
                    recognitionRequest?.appendAudioPCMBuffer(buffer)
                }
            }

            audioEngine.prepare()
            audioEngine.startAndReturnError(null)

            recognitionTask =
                speechRecognizer?.recognitionTaskWithRequest(recognitionRequest!!) { result, error ->
                    if (error != null) {
                        emitter.trySend("Error: ${error.localizedDescription}")
                        emitter.close()
                        return@recognitionTaskWithRequest
                    }
                    if (result != null) {
                        emitter.trySend(result.bestTranscription.formattedString)
                        if (result.isFinal()) {
                            emitter.close() // Close flow after final result
                        }
                    }
                }

        } catch (e: Exception) {
            emitter.trySend("Error starting audio engine: ${e.message}")
            emitter.close(e)
        }
    }


    actual fun stopListening() {
        audioEngine.stop()
        recognitionRequest?.endAudio()
        recognitionTask?.cancel() // This might be better as finish or cancel
        recognitionTask = null
        recognitionRequest = null
    }

    actual fun cancel() {
        audioEngine.stop()
        recognitionTask?.cancel()
        recognitionTask = null
        recognitionRequest = null
    }
}