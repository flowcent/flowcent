package com.aiapp.flowcent.core.platform

import com.aiapp.flowcent.core.data.ContentBlock
import com.aiapp.flowcent.core.data.ContentPart
import com.aiapp.flowcent.core.data.GeminiContentRequest
import com.aiapp.flowcent.chat.domain.model.ChatResult
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

actual class FlowCentAi {
    /**
     * Sends a prompt to the generative AI model and returns a structured result.
     * @param prompt The input text prompt for the AI.
     * @return A [Result] containing [ChatResult] on success, or an [Exception] on failure.
     */


//    private val generativeModel = Firebase.ai(backend = GenerativeBackend.googleAI())
//        .generativeModel("gemini-2.0-flash")

//    private val generativeModel = GenerativeModel(
//        modelName = "gemma-3-27b-it", apiKey = "AIzaSyDgyl1Ir3VxoXid_cdP57EF67-troYy7oI"
//    )

//@Sohan
//@Important note:
//Firebase sdk for ios side of compose multiplatform and ai logic
// still has not published. That's why, for now we are doing it with the Gemini
// rest api for the ios part.
//When Firebase ai logic will be available, we have to replace it with it

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    private val apiKey = "AIzaSyDgyl1Ir3VxoXid_cdP57EF67-troYy7oI"

    private val json = Json {
        ignoreUnknownKeys = true
    }


    actual suspend fun generateContent(prompt: String): Result<ChatResult> {
        return try {
            val url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

            val requestBody = GeminiContentRequest(
                contents = listOf(
                    ContentBlock(
                        parts = listOf(
                            ContentPart(text = prompt)
                        )
                    )
                )
            )

            val response: HttpResponse = client.post(url) {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }
            val responseText = response.bodyAsText()

            // 🔍 Extracting plain JSON text if it's wrapped in content format
            val jsonElement = json.parseToJsonElement(responseText)
            val textOutput = jsonElement
                .jsonObject["candidates"]
                ?.jsonArray?.getOrNull(0)
                ?.jsonObject?.get("content")
                ?.jsonObject?.get("parts")
                ?.jsonArray?.getOrNull(0)
                ?.jsonObject?.get("text")
                ?.jsonPrimitive?.content
                ?: throw IllegalStateException("Missing expected response content")

            val cleanJson = cleanJsonFromMarkdown(textOutput)
            val chatResult = json.decodeFromString<ChatResult>(cleanJson)
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