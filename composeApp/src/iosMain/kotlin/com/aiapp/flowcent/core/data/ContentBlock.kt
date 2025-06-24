/*
 * Created by Saeedus Salehin on 24/6/25, 12:04 PM.
 */

package com.aiapp.flowcent.core.data

import kotlinx.serialization.Serializable

@Serializable
data class ContentBlock(
    val parts: List<ContentPart>
)