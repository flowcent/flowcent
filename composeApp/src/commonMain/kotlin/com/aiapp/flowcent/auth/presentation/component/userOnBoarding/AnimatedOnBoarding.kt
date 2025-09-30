package com.aiapp.flowcent.auth.presentation.component.userOnBoarding

import KottieAnimation
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import flowcent.composeapp.generated.resources.Res
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import utils.KottieConstants

@Composable
fun AnimatedChatOnboarding() {
    var botOnboardingAnimation by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        botOnboardingAnimation = Res.readBytes("files/botonboardinganimation.json").decodeToString()
    }

    val botOnboardingComposition = rememberKottieComposition(
        spec = KottieCompositionSpec.File(botOnboardingAnimation) // Or KottieCompositionSpec.Url || KottieCompositionSpec.JsonString
    )

    val botAnimationState by animateKottieCompositionAsState(
        composition = botOnboardingComposition,
        iterations = KottieConstants.IterateForever
    )

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KottieAnimation(
            composition = botOnboardingComposition,
            progress = { botAnimationState.progress },
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Meet your AI Assistant",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Chat naturally about your daily \n" +
                    "expenses and let AI automatically \n" +
                    "categorize and track your spending",
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                color = Color.Gray
            ),
        )
    }
}