package com.aiapp.flowcent.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times

@Composable
fun OverlappingAvatars() {
    val avatars = listOf(
        Triple("CC", Color(0xFF6D5DFB), 0),
        Triple("👥", Color(0xFF9B5DE5), 1),
        Triple("CC", Color(0xFFFF9F1C), 2)
    )

    Row(
        modifier = Modifier
            .background(Color(0xFFF5F5F5), shape = CircleShape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            val avatarSize = 28.dp
            val overlapOffset = 16.dp

            avatars.forEach { (text, borderColor, index) ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(avatarSize)
                        .offset(x = (index * overlapOffset))
                        .clip(CircleShape)
                        .border(2.dp, borderColor, CircleShape)
                        .background(Color.White)
                ) {
                    Text(
                        text = text,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = "Next",
            tint = Color.Gray
        )
    }
}
