package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.domain.utils.EnumConstants
import com.aiapp.flowcent.core.presentation.components.SelectionToggle

@Composable
fun SpendingList(
    expenseItems: List<ExpenseItem>,
    modifier: Modifier = Modifier
) {
    val isSelected = remember { mutableStateOf(false) }

    fun handleSelect() {
        isSelected.value = !isSelected.value
    }

    expenseItems.map { expenseItem ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(8.dp)
                ).padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SelectionToggle(
                            isSelected = isSelected.value,
                            onToggle = { handleSelect() }
                        )
                        Column(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                expenseItem.title,
                                color = Color.Black,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                expenseItem.category,
                                color = Color.Black,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                Text(
                    expenseItem.amount.toString(),
                    color = when (expenseItem.type) {
                        EnumConstants.TransactionType.INCOME, EnumConstants.TransactionType.BORROW -> Color.Green
                        EnumConstants.TransactionType.EXPENSE, EnumConstants.TransactionType.LEND -> Color.Red
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}