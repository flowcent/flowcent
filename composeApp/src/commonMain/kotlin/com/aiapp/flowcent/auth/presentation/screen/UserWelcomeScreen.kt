package com.aiapp.flowcent.auth.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.PartyMode
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.core.presentation.components.AppButton
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.FeatureList
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.applogo
import org.jetbrains.compose.resources.painterResource

data class FeatureItem(
    val icon: ImageVector,
    val iconBackground: Color,
    val title: String,
)

@Composable
fun UserWelcomeScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel
) {
    val features = listOf(
        FeatureItem(
            icon = Icons.Default.WavingHand,
            iconBackground = Color(0xffe8a40e),
            title = "Let's get introduced",
        ),
        FeatureItem(
            icon = Icons.Default.Chat,
            iconBackground = Color(0xFFA855F7),
            title = "Chat with AI about expenses and earnings",
        ),
        FeatureItem(
            icon = Icons.Default.Group,
            iconBackground = Color(0xFF3B82F6),
            title = "Create shared account",
        )
    )

    Box(
        modifier = modifier.fillMaxSize().padding(24.dp),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(Res.drawable.applogo),
                contentDescription = "Wallet Icon",
                modifier = Modifier.size(100.dp).clip(RoundedCornerShape(16.dp))
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Welcome to Flowcent",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Let's setup your personal finance goals in just a few steps",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            FeatureList(features)
        }

        Column(
            modifier = Modifier.align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppButton(
                onClick = {
                    viewModel.onAction(UserAction.NavigateToUserOnboard)
                },
                height = 60.dp,
                text = "Get Started",
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontWeight = FontWeight.SemiBold
                ),
                hasGradient = true,
                textColor = MaterialTheme.colorScheme.onPrimary,
            )
        }
    }
}

