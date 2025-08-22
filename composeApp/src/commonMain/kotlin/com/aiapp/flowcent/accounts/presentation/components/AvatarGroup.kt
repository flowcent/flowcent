package com.aiapp.flowcent.accounts.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.zIndex
import com.aiapp.flowcent.core.presentation.components.NameInitial

@Composable
fun AvatarGroup(
    members: List<String>,
    avatarSize: Dp = 28.dp,
    textSize: TextUnit = 12.sp,
    modifier: Modifier = Modifier
) {
    val visibleMembers = members.take(4)
    val remainingCount = members.size - visibleMembers.size
    val overlap = avatarSize / 2

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(avatarSize),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Box(
            modifier = Modifier.width(
                avatarSize + (visibleMembers.size + if (remainingCount > 0) 1 else 0 - 1) * overlap
            )
        ) {
            visibleMembers.forEachIndexed { index, initial ->
                Box(
                    modifier = Modifier
                        .offset(x = overlap * index)
                        .zIndex(index.toFloat())
                ) {
                    NameInitial(
                        text = initial,
                        textSize = textSize,
                        size = avatarSize
                    )
                }
            }

            if (remainingCount > 0) {
                Box(
                    modifier = Modifier
                        .offset(x = overlap * visibleMembers.size)
                        .zIndex(visibleMembers.size.toFloat())
                        .size(avatarSize)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+$remainingCount",
                        fontSize = textSize,
                        color = Color.White
                    )
                }
            }
        }
    }
}

