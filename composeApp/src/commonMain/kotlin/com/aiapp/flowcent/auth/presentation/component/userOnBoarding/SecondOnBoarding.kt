package com.aiapp.flowcent.auth.presentation.component.userOnBoarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wallet
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.presentation.components.LabeledInputField

@Composable
fun SecondOnBoarding(
    modifier: Modifier = Modifier,
    currentBalance: Double = 0.0,
    savingGoal: Double = 0.0,
    currentBalanceError: String? = null,
    savingTargetError: String? = null,
    onUpdateCurrentBalance: (String) -> Unit,
    onUpdateSavingGoal: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(80.dp).background(
                color = MaterialTheme.colorScheme.primary, shape = CircleShape
            ), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Wallet,
                contentDescription = "Wallet Icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(40.dp)
            )

        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Let's set up your finances",
            style = MaterialTheme.typography.headlineMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Tell us about your current balance and saving goal",
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        LabeledInputField(
            label = "Current Balance",
            placeholder = "$0",
            isNumeric = true,
            value = if (currentBalance > 0.0) {
                if (currentBalance % 1.0 == 0.0) {
                    currentBalance.toInt().toString()
                } else {
                    currentBalance.toString()
                }
            } else "",
            onValueChange = {
                onUpdateCurrentBalance(it)
            },
        )

        if (currentBalanceError != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = currentBalanceError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        LabeledInputField(
            label = "Monthly Saving Goal",
            placeholder = "$0",
            isNumeric = true,
            value = if (savingGoal > 0.0) {
                if (savingGoal % 1.0 == 0.0) {
                    savingGoal.toInt().toString()
                } else {
                    savingGoal.toString()
                }
            } else "",
            onValueChange = {
                onUpdateSavingGoal(it)
            },
        )

        if (savingTargetError != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = savingTargetError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
