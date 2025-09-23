package com.aiapp.flowcent.core.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.aiapp.flowcent.core.domain.utils.EnumConstants
import com.aiapp.flowcent.core.presentation.ui.theme.Colors

@Composable
fun ExpenseAmount(amount: Double, type: EnumConstants.TransactionType) {
    val formattedAmount = if (amount % 1.0 == 0.0) {
        amount.toInt().toString()
    } else {
        amount.toString()
    }


    Text(
        text = formattedAmount,
        color = when (type) {
            EnumConstants.TransactionType.INCOME,
            EnumConstants.TransactionType.BORROW -> Colors.IncomeColor

            EnumConstants.TransactionType.EXPENSE,
            EnumConstants.TransactionType.LEND -> Colors.ExpenseColor
        },
        style = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Bold
        )
    )
}
