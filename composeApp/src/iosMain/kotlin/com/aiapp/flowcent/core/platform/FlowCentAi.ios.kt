package com.aiapp.flowcent.core.platform

import kotlinx.serialization.Serializable

@Serializable
data class GeminiContentRequest(
    val contents: List<ContentBlock>
)

@Serializable
data class ContentBlock(
    val parts: List<ContentPart>
)

@Serializable
data class ContentPart(
    val text: String
)

