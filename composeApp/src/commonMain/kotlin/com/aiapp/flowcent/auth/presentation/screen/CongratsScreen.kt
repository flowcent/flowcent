package com.aiapp.flowcent.auth.presentation.screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.FirstBadge
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.SuccessAnimation
import com.aiapp.flowcent.core.presentation.components.AppButton
import com.aiapp.flowcent.subscription.presentation.PurchaseUserAction
import com.aiapp.flowcent.subscription.presentation.SubscriptionViewModel
import com.aiapp.flowcent.subscription.presentation.component.RcPaywall
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.bulb
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CongratsScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    state: AuthState,
    subscriptionVM: SubscriptionViewModel
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    fun handleHideBottomSheet() {
        viewModel.onAction(UserAction.ShowPaymentSheet(false))
        viewModel.onAction(UserAction.NavigateToHome)
    }

    LaunchedEffect(key1 = state.showPaymentSheet) {
        if (state.showPaymentSheet) {
            coroutineScope.launch {
                bottomSheetState.show()
            }
        } else {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            SuccessAnimation()

            Spacer(modifier = Modifier.height(48.dp))

            // Title
            Text(
                text = "Congratulations!",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "You've recorded your first transaction",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Great job! You're on your way to better financial tracking and budgeting.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Achievement card
            FirstBadge()
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                AppButton(
                    text = "Continue",
                    onClick = {
                        viewModel.onAction(UserAction.ShowPaymentSheet(true))
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            Divider(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            // Pro Tip
            Spacer(Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0XFF2F2523)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.bulb),
                        contentDescription = null,
                        tint = Color(0XFFFACC14)
                    )
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Pro Tip",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "Try to record transactions as soon as you make them for the most accurate tracking.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }

    if (state.showPaymentSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                handleHideBottomSheet()
            },
            sheetState = bottomSheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxSize()
        ) {
            RcPaywall(
                onUpdatePlan = { customerInfo ->
                    subscriptionVM.onAction(
                        PurchaseUserAction.UpdateCurrentPlan(
                            state.uid,
                            customerInfo,
                            true
                        )
                    )
                },
                onDismiss = {
                    handleHideBottomSheet()
                }
            )
        }
    }
}