package com.aiapp.flowcent.userOnboarding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.chat.domain.model.ChatMessage
import com.aiapp.flowcent.chat.presentation.components.BotMessageContent
import com.aiapp.flowcent.chat.presentation.components.UserMessage
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.domain.utils.EnumConstants
import com.aiapp.flowcent.core.presentation.components.NameInitial
import com.aiapp.flowcent.core.presentation.components.ShimmerChat
import com.aiapp.flowcent.core.presentation.ui.theme.Colors
import com.aiapp.flowcent.userOnboarding.UserObViewModel
import com.aiapp.flowcent.userOnboarding.UserOnboardingState

@Composable
fun ChatOnboardItem(
    message: ChatMessage,
    state: UserOnboardingState,
    viewModel: UserObViewModel,
    modifier: Modifier = Modifier
) {
    when (message) {
        is ChatMessage.ChatUserMessage -> {
            UserMessage(
                avatarName = "Flowcent", text = message.text
            )
        }

        is ChatMessage.ChatBotMessage -> {
            if (message.isLoading) {
                BotMessageContent(
                    avatarName = "Marcus", modifier = modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    ShimmerChat()
                }

            } else {
                Column(
                    modifier = modifier.fillMaxWidth().padding(top = 24.dp),
                ) {
                    BotContentRow(
                        name = "Marcus", message = message.text
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    if (message.expenseItems.isNotEmpty()) {
                        message.expenseItems.forEach { expenseItem ->
                            ExpenseItemRow(expenseItem = expenseItem)
                        }
                    }
                }
            }

        }
    }

}


@Composable
fun BotContentRow(
    name: String,
    message: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NameInitial(
            text = name,
            textSize = 16.sp,
            size = 40.dp,
            modifier = Modifier.padding(start = 12.dp)
        )

        Text(
            text = message,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun ExpenseItemRow(expenseItem: ExpenseItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExpenseItemDetails(
                    title = expenseItem.title,
                    category = expenseItem.category
                )

                ExpenseAmount(
                    amount = expenseItem.amount,
                    type = expenseItem.type
                )
            }

            Spacer(Modifier.height(12.dp))

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )
        }
    }
}

@Composable
fun ExpenseItemDetails(title: String, category: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = category,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun ExpenseAmount(amount: Double, type: EnumConstants.TransactionType) {
    Text(
        text = amount.toString(),
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
