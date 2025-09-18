package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.core.presentation.components.NameInitial

@Composable
fun BotMessageContent(
    modifier: Modifier = Modifier,
    avatarName: String = "Marcus",
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        NameInitial(
            text = avatarName,
            textSize = 16.sp,
            size = 40.dp,
            modifier = Modifier.padding(start = 12.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        content()
    }
}

