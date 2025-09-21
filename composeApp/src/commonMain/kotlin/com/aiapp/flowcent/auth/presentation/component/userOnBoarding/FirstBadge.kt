package com.aiapp.flowcent.auth.presentation.component.userOnBoarding

import KottieAnimation
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import flowcent.composeapp.generated.resources.Res
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import utils.KottieConstants

@Composable
fun FirstBadge() {
    var firstBadgeAnimation by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        firstBadgeAnimation = Res.readBytes("files/firstbadge.json").decodeToString()
    }

    val firstBadgeComposition = rememberKottieComposition(
        spec = KottieCompositionSpec.File(firstBadgeAnimation) // Or KottieCompositionSpec.Url || KottieCompositionSpec.JsonString
    )

    val firstBadgeAnimationState by animateKottieCompositionAsState(
        composition = firstBadgeComposition,
        iterations = KottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFF5769F2), Color(0xFF8442EC))
                )
            )
            .border(2.dp, Color(0xFF5769F2), RoundedCornerShape(24.dp))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                KottieAnimation(
                    composition = firstBadgeComposition,
                    progress = { firstBadgeAnimationState.progress },
                    modifier = Modifier.size(50.dp)
                )
            }
            Column {
                Text(
                    text = "First Entry",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                Text(
                    text = "Achievement Unlocked",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White.copy(alpha = 0.7f)
                    )
                )
            }
        }
    }
}