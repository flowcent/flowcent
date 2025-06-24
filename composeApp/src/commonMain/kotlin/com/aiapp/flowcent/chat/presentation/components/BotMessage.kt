package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.ui.theme.Colors
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.outline_charger
import org.jetbrains.compose.resources.painterResource

@Composable
fun BotMessage(text: String, isRich: Boolean = false) {
    Row(modifier = Modifier.padding(vertical = 4.dp)) {
        Icon(
            painter = painterResource(Res.drawable.outline_charger),
            contentDescription = null,
            tint = Colors.Black,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            shape = RoundedCornerShape(12.dp), color = Color(0xFF1E1E1E)
        ) {
            Text(
                text = text,
                color = Colors.White,
                modifier = Modifier.padding(12.dp),
                style = if (isRich) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall
            )
        }
    }
}
