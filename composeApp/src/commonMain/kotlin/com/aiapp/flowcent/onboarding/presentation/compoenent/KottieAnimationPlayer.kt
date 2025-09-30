package com.aiapp.flowcent.onboarding.presentation.compoenent

import KottieAnimation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import flowcent.composeapp.generated.resources.Res
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import utils.KottieConstants
import androidx.compose.ui.Modifier.Companion as Modifier1

@Composable
fun KottieAnimationPlayer(
    filePath: String,
    modifier: Modifier = Modifier1,
    iterations: Int = KottieConstants.IterateForever
) {
    var animationJson by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(filePath) {
        animationJson = Res.readBytes(filePath).decodeToString()
    }

    animationJson?.let { json ->
        val composition = rememberKottieComposition(
            spec = KottieCompositionSpec.JsonString(json)
        )

        val animationState by animateKottieCompositionAsState(
            composition = composition,
            iterations = iterations
        )

        // Render the animation
        KottieAnimation(
            composition = composition,
            progress = { animationState.progress },
            modifier = modifier
        )
    }
}
