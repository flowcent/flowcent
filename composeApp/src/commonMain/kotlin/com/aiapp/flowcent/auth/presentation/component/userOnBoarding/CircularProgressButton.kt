package com.aiapp.flowcent.auth.presentation.component.userOnBoarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressButton(
    progress: Float,
    onClick: () -> Unit
) {
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "")

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(64.dp)
            .clickable(onClick = onClick)
    ) {
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.size(64.dp),
            strokeWidth = 3.dp,
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color(0xFFE0E0E0)
        )
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.primary
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}