package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.data.common.ExpenseItem

@Composable
fun SpendingCard(expenseItem: ExpenseItem) {
    Card(
        backgroundColor = Color(0xFF1E1E1E),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    expenseItem.title,
                    color = Color(0xFFFFFFFF),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    expenseItem.category,
                    color = Color(0xFF808080),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                expenseItem.amount.toString(),
                color = Color(0xFFFFFFFF),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}