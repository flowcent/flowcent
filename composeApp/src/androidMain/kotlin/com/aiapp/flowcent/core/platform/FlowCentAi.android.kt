package com.aiapp.flowcent.core.platform

import com.aiapp.flowcent.data.common.ChatResult
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import com.google.firebase.ai.type.content
import kotlinx.serialization.json.Json

actual class FlowCentAi {
    /**
     * Sends a prompt to the generative AI model and returns a structured result.
     * @param prompt The input text prompt for the AI.
     * @return A [Result] containing [ChatResult] on success, or an [Exception] on failure.
     */

    private val generativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.0-flash")

    actual suspend fun generateContent(prompt: String): Result<ChatResult> {
        return try {
            val response = generativeModel.generateContent(content { text(prompt) })
            val rawJson = response.text ?: throw IllegalStateException("No response text")
            val cleanJson = cleanJsonFromMarkdown(rawJson)
            val chatResult = Json.decodeFromString<ChatResult>(cleanJson)
            Result.success(chatResult)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun cleanJsonFromMarkdown(text: String): String {
        return text.trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()
    }
}