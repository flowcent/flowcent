package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.presentation.components.ExpenseAmount
import com.aiapp.flowcent.core.presentation.components.ExpenseItemDetails
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.cent
import org.jetbrains.compose.resources.painterResource


@Composable
fun SpendingList(
    expenseItems: List<ExpenseItem>,
    checkedExpenseItems: List<ExpenseItem>,
    onCheckedItem: (Boolean, ExpenseItem) -> Unit,
    onEdit: (ExpenseItem) -> Unit,
    onDelete: (ExpenseItem) -> Unit,
) {

    expenseItems.mapIndexed { index, expenseItem ->
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = checkedExpenseItems.contains(expenseItem),
                onCheckedChange = { isChecked ->
                    onCheckedItem(isChecked, expenseItem)
                },
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExpenseItemDetails(
                    title = expenseItem.title,
                    category = expenseItem.category
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.cent),
                        contentDescription = "Cent",
                        tint = Color(0xffe8a40e),
                        modifier = Modifier.size(18.dp)
                    )

                    ExpenseAmount(
                        amount = expenseItem.amount,
                        type = expenseItem.type
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}