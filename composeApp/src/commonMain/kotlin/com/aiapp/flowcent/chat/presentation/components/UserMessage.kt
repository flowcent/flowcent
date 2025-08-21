package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    ) {

        NameInitial(
            text = "TH",
            textSize = 16.sp,
            size = 40.dp,
            modifier = Modifier.padding(start = 12.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))


        Column(
            modifier = Modifier.background(Color(0xFF2C2C2E), shape = RoundedCornerShape(12.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 14
            )
        }

    }
}