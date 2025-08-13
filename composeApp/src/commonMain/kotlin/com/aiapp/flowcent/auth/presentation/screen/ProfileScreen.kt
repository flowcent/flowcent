package com.aiapp.flowcent.auth.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.core.presentation.components.AppButton
import com.aiapp.flowcent.core.presentation.components.NameInitial
import flowcent.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    authState: AuthState
) {
    LaunchedEffect(Unit) {
        authViewModel.onAction(UserAction.FetchUserId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            // Profile Image
//        Image(
//            painter = painterResource("profile_picture.png"), // Replace with actual image
//            contentDescription = "Profile Picture",
//            modifier = Modifier
//                .size(100.dp)
//                .clip(CircleShape)
//        )
            item {
                NameInitial(
                    text = authState.user?.localUserName ?: "",
                    textSize = 60.sp,
                    size = 150.dp
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                // Name
                Text(
                    text = authState.user?.localUserName ?: "",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))
            }


            item {
                ProBadgeButton(
                    onClick = {
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // MemberShip Section
            item { SectionHeader("MemberShip") }
            item {
                SettingsCard(
                    items = listOf(
                        "Free trial" to "tips_icon.png",
                        "Pro" to "faq_icon.png",
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // PERSONALIZE Section
            item { SectionHeader("PERSONALIZE") }
            item {
                SettingsCard(
                    items = listOf(
                        "Personal Details" to "personal_icon.png",
                        "Heart Rate Zones" to "heart_rate_icon.png",
                        "Settings" to "settings_icon.png"
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            // NEED HELP? Section
            item { SectionHeader("NEED HELP?") }
            item {
                SettingsCard(
                    items = listOf(
                        "Privacy Policy" to "tips_icon.png",
                        "Terms and Condition" to "tips_icon.png",
                        "Tips and Tricks" to "tips_icon.png",
                        "Frequently Asked Questions" to "faq_icon.png",
                        "Contact Us" to "contact_icon.png"
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppButton(
                onClick = {
                    authViewModel.onAction(UserAction.FirebaseSignOut)
                },
                text = "Sign Out",
                backgroundColor = Color(0xFF0E0E0E),
                isLoading = authState.isEmailSignInProcessing,
            )

        }


    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    )
}

@Composable
fun SettingsCard(items: List<Pair<String, String>>) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF7F7F7))
    ) {
        items.forEachIndexed { index, (title, _) ->
            SettingItem(
                title = title,
                icon = "", // Replace with actual icon resource
                modifier = Modifier
                    .clickable { }
                    .padding(horizontal = 12.dp, vertical = 14.dp)
            )
            if (index != items.lastIndex) {
                Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            }
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    icon: String,
    trailingContent: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon placeholder
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(Color.LightGray, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        trailingContent?.invoke()
    }

}


@Composable
fun ProBadgeButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF0F2027),
                        Color(0xFF203A43),
                        Color(0xFF2C5364)
                    )
                )
            )
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star, // Replace with your icon
                        contentDescription = null,
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Become a Flowcent Pro",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFD700)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Upgrade to use unlimited transactions recording with AI voice and chat",
                    fontSize = 13.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
            Text(
                text = "See how",
                fontSize = 13.sp,
                color = Color.White.copy(alpha = 0.9f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

