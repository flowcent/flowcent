package com.aiapp.flowcent.core.data

import kotlinx.serialization.Serializable

@Serializable
data class GeminiContentRequest(
    val contents: List<ContentBlock>
)

