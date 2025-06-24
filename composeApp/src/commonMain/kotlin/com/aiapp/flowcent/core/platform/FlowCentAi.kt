package com.aiapp.flowcent.core.platform

import com.aiapp.flowcent.chat.domain.model.ChatResult

expect class FlowCentAi{
    /**
     * Sends a prompt to the generative AI model and returns a structured result.
     * @param prompt The input text prompt for the AI.
     * @return A [Result] containing [ChatResult] on success, or an [Exception] on failure.
     */
    suspend fun generateContent(prompt: String): Result<ChatResult>
}