package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
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
fun SpendingList(
    expenseItems: List<ExpenseItem>,
    checkedExpenseItems: List<ExpenseItem>,
    onCheckedItem: (Boolean, ExpenseItem) -> Unit,
    modifier: Modifier = Modifier
) {
    expenseItems.map { expenseItem ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
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

                        Checkbox(
                            checked = checkedExpenseItems.contains(expenseItem),
                            onCheckedChange = { isChecked ->
                                onCheckedItem(isChecked, expenseItem)
                            }
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
                        EnumConstants.TransactionType.INCOME,
                        EnumConstants.TransactionType.BORROW -> Colors.IncomeColor

                        EnumConstants.TransactionType.EXPENSE,
                        EnumConstants.TransactionType.LEND -> Colors.ExpenseColor
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth().height(12.dp))
    }
}