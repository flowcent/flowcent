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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.auth.util.calculateRemainingCredits
import com.aiapp.flowcent.core.presentation.components.AppButton
import com.aiapp.flowcent.core.presentation.components.NameInitial
import com.aiapp.flowcent.core.presentation.components.SubscriptionBadge
import com.aiapp.flowcent.subscription.presentation.PurchaseUserAction
import com.aiapp.flowcent.subscription.presentation.SubscriptionViewModel
import com.aiapp.flowcent.subscription.presentation.component.RcPaywall
import com.aiapp.flowcent.subscription.util.SubscriptionUtil
import com.aiapp.flowcent.subscription.util.SubscriptionUtil.FREE_ENTITLEMENT_ID
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    authState: AuthState,
    subscriptionVM: SubscriptionViewModel
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var transactionCreditsRemaining by remember { mutableStateOf(0) }
    var aiChatCreditsRemaining by remember { mutableStateOf(0) }


    fun handleHideBottomSheet() {
        authViewModel.onAction(UserAction.ShowPaymentSheet(false))
    }

    LaunchedEffect(key1 = authState.showPaymentSheet) {
        if (authState.showPaymentSheet) {
            coroutineScope.launch {
                bottomSheetState.show()
            }
        } else {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        }
    }

    LaunchedEffect(Unit) {
        authViewModel.onAction(UserAction.FetchUserId)
    }

    LaunchedEffect(key1 = authState.user) {
        if (authState.user != null) {
            val features = SubscriptionUtil.getSubscriptionFeatures(
                    SubscriptionUtil.getSubscriptionPlan(
                        authState.user.subscription.currentEntitlementId
                    )
                )
            val (remainingCredits, remainingAiChatCredits) = calculateRemainingCredits(
                authState.user.totalRecordCredits, authState.user.totalAiChatCredits, features
            )

            transactionCreditsRemaining = remainingCredits
            aiChatCreditsRemaining = remainingAiChatCredits
        }
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        LazyColumn(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            item {
                NameInitial(
                    text = authState.user?.localUserName ?: "", textSize = 50.sp, size = 150.dp
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                // Name
                Text(
                    text = authState.user?.localUserName ?: "",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))
            }


            item {
                SubscriptionBadge {
                    authViewModel.onAction(UserAction.ShowPaymentSheet(true))
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // MemberShip Section
            item { SectionHeader("MemberShip") }
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(vertical = 16.dp, horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = authState.user?.subscription?.currentPlan ?: FREE_ENTITLEMENT_ID,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "$transactionCreditsRemaining Transactions Credits Remaining",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "$aiChatCreditsRemaining AI Chat Credits Remaining",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // PERSONALIZE Section
            item {  Spacer(modifier = Modifier.height(16.dp)) }
            item { SectionHeader("PERSONALIZE") }
            item {
                Spacer(modifier = Modifier.height(8.dp))
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
                Spacer(modifier = Modifier.height(8.dp))
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
                isLoading = authState.isEmailSignInProcessing,
            )

        }
    }

    if (authState.showPaymentSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                handleHideBottomSheet()
            },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize()
        ) {
            RcPaywall(onUpdatePlan = { customerInfo ->
                subscriptionVM.onAction(
                    PurchaseUserAction.UpdateCurrentPlan(
                        authState.uid, customerInfo, true
                    )
                )
            }, onDismiss = {
                handleHideBottomSheet()
            })
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title, style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold
        ), color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SettingsCard(items: List<Pair<String, String>>) {
    Column(
        modifier = Modifier.clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        items.forEachIndexed { index, (title, _) ->
            SettingItem(
                title = title, icon = "", // Replace with actual icon resource
                modifier = Modifier.clickable { }.padding(horizontal = 12.dp, vertical = 14.dp)
            )
            if (index != items.lastIndex) {
                Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
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
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon placeholder
        Box(
            modifier = Modifier.size(24.dp)
                .background(MaterialTheme.colorScheme.onSurfaceVariant, CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        trailingContent?.invoke()
    }
}

