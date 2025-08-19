package com.aiapp.flowcent.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.noInternet
import org.jetbrains.compose.resources.painterResource

@Composable
fun NoInternet(
    imagePainter: Painter = painterResource(Res.drawable.noInternet),
    imageSize: Dp = 200.dp,
    buttonName: String = "0k",
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image
        Image(
            painter = imagePainter,
            contentDescription = "No Internet",
            modifier = Modifier.size(imageSize)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Message
        Text(
            text = "Please double check your internet connection or try again later.",
            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
            textAlign = TextAlign.Center,
            maxLines = 4
        )

        Spacer(modifier = Modifier.height(16.dp))

        // OK Button
        Text(
            text = buttonName,
            fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
            color = Color.Red,
            modifier = Modifier.clickable { onButtonClick() })
    }
}
