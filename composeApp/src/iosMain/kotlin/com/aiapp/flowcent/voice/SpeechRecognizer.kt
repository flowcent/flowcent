package com.aiapp.flowcent.voice

import platform.AVFoundation.AVAudioSession
import platform.AVFoundation.AVAudioSessionCategoryRecord
import platform.AVFoundation.AVAudioSessionModeMeasurement
import platform.AVFoundation.AVAudioSessionPortOverrideNone
import platform.AVFoundation.setCategory
import platform.AVFoundation.setActive
import platform.Speech.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import platform.Foundation.NSError
import platform.Foundation.locale
import platform.darwin.dispatch_get_main_queue

actual class SpeechRecognizer {

    private var speechRecognizer: SFSpeechRecognizer? = null
    private var recognitionRequest: SFSpeechAudioBufferRecognitionRequest? = null
    private var recognitionTask: SFSpeechRecognitionTask? = null
    private val audioEngine = platform.AVFoundation.AVAudioEngine()

    actual fun isRecognitionAvailable(): Boolean {
        return SFSpeechRecognizer.isSupported()
    }

    actual fun startListening(): Flow<String> = callbackFlow {
        if (!isRecognitionAvailable()) {
            send("Speech recognition not available")
            close()
            return@callbackFlow
        }

        // Request authorization
        SFSpeechRecognizer.requestAuthorization { status ->
            if (status == SFSpeechRecognizerAuthorizationStatusAuthorized) {
                launch(dispatch_get_main_queue().asCoroutineDispatcher()) { // Ensure UI updates on main thread
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

    private fun startAudioEngineAndRecognize(emitter: FlowCollector<String>) {
        try {
            // Configure audio session
            val audioSession = AVAudioSession.sharedInstance()
            audioSession.setCategory(AVAudioSessionCategoryRecord, error = null)
            audioSession.setMode(AVAudioSessionModeMeasurement, error = null)
            audioSession.setActive(true, options = 0U, error = null)

            speechRecognizer = SFSpeechRecognizer(locale = platform.Foundation.NSLocale.currentLocale())
            recognitionRequest = SFSpeechAudioBufferRecognitionRequest()
            recognitionRequest?.setShouldReportPartialResults(true)

            val inputNode = audioEngine.inputNode
            val recordingFormat = inputNode.outputFormatForBus(0U)
            inputNode.installTapOnBus(
                bus = 0U,
                bufferSize = 1024U,
                format = recordingFormat
            ) { buffer, _ ->
                recognitionRequest?.appendAudioPCMBuffer(buffer)
            }

            audioEngine.prepare()
            audioEngine.startAndReturnError(null)

            recognitionTask = speechRecognizer?.recognitionTaskWithRequest(recognitionRequest!!) { result, error ->
                if (error != null) {
                    emitter.trySend("Error: ${error.localizedDescription}")
                    emitter.close(Exception(error.localizedDescription))
                    return@recognitionTaskWithRequest
                }
                if (result != null) {
                    emitter.trySend(result.bestTranscription.formattedString)
                    if (result.isFinal) {
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