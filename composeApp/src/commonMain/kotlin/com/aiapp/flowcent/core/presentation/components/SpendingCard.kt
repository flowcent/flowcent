package com.aiapp.flowcent.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.domain.utils.EnumConstants
import com.aiapp.flowcent.core.presentation.ui.theme.Colors

@Composable
fun SpendingCard(
    createdBy: String = "",
    expenseItem: ExpenseItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Color.Transparent
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.padding(end = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (createdBy.isNotEmpty()) {
                    NameInitial(text = createdBy)
                }

                Spacer(modifier = modifier.width(12.dp))

                Column {
                    Text(
                        expenseItem.title,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        expenseItem.category,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Text(
                text = expenseItem.amount.toString(),
                color = when (expenseItem.type) {
                    EnumConstants.TransactionType.INCOME,
                    EnumConstants.TransactionType.BORROW -> Colors.IncomeColor

                    EnumConstants.TransactionType.EXPENSE,
                    EnumConstants.TransactionType.LEND -> Colors.ExpenseColor
                },
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}