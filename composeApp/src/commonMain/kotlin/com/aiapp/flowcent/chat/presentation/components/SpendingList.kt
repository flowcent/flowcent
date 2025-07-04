package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.domain.model.ExpenseItem

@Composable
fun SpendingList(
    expenseItems: List<ExpenseItem>
) {
    expenseItems.map { expenseItem ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
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
                Text(
                    expenseItem.amount.toString(),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (expenseItems.size > 1) {
                HorizontalDivider(
                    modifier = Modifier.height(1.dp),
                    color = Color.Gray
                )
            }
        }
    }
}