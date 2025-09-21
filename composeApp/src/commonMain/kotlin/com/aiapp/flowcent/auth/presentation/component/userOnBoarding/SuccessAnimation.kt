package com.aiapp.flowcent.auth.presentation.component.userOnBoarding

import KottieAnimation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import flowcent.composeapp.generated.resources.Res
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import utils.KottieConstants

@Composable
fun SuccessAnimation(){
    var successAnimation by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        successAnimation = Res.readBytes("files/successAnimation.json").decodeToString()
    }

    val successComposition = rememberKottieComposition(
        spec = KottieCompositionSpec.File(successAnimation) // Or KottieCompositionSpec.Url || KottieCompositionSpec.JsonString
    )

    val successAnimationState by animateKottieCompositionAsState(
        composition = successComposition,
        iterations = KottieConstants.IterateForever
    )

    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        KottieAnimation(
            composition = successComposition,
            progress = { successAnimationState.progress },
            modifier = Modifier.size(200.dp)
        )
    }

}