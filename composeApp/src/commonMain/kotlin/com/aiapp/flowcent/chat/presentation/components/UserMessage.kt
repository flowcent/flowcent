package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.core.Spacing
import com.aiapp.flowcent.core.presentation.components.NameInitial

@Composable
fun UserMessage(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp), color = Color.White
        ) {
            Text(
                text = text,
                color = Color.Black,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.padding(Spacing.small))

        NameInitial(
            text = "TH",
            textSize = 16.sp,
            size = 40.dp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}