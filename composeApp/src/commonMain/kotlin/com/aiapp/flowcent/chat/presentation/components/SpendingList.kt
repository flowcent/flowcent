package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.domain.utils.EnumConstants
import com.aiapp.flowcent.core.presentation.ui.theme.Colors
import kotlin.math.roundToInt


@Composable
fun rememberSwipeState(maxSwipe: Dp = 160.dp): AnchoredDraggableState<Int> {
    val density = LocalDensity.current
    val maxSwipePx = with(density) { maxSwipe.toPx() }

    // DSL is: <State> at <PositionInPx>
    val anchors = remember(maxSwipePx) {
        DraggableAnchors {
            0 at 0f              // closed
            1 at -maxSwipePx     // open (revealed to the left)
        }
    }

    return remember {
        AnchoredDraggableState(
            initialValue = 0,
            anchors = anchors,
            confirmValueChange = { true }
        )
    }
}


@Composable
fun SpendingList(
    expenseItems: List<ExpenseItem>,
    checkedExpenseItems: List<ExpenseItem>,
    onCheckedItem: (Boolean, ExpenseItem) -> Unit,
    onEdit: (ExpenseItem) -> Unit,
    onDelete: (ExpenseItem) -> Unit,
    modifier: Modifier = Modifier
) {

    expenseItems.map { expenseItem ->
        val anchorState = rememberSwipeState()
        Box(
            modifier = modifier
                .fillMaxWidth()
                .anchoredDraggable(
                    state = anchorState,
                    orientation = Orientation.Horizontal
                )
        ) {

            // BACKGROUND (actions)
            Row(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onEdit(expenseItem) }) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.width(12.dp))
                IconButton(onClick = { onDelete(expenseItem) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Colors.Red300
                    )
                }
            }

            // FOREGROUND (the row)
            val offsetX = anchorState.requireOffset().roundToInt()
            Row(
                modifier = Modifier
                    .offset { IntOffset(offsetX, 0) }
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
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
                                        color = MaterialTheme.colorScheme.onSurface,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        expenseItem.category,
                                        color = MaterialTheme.colorScheme.onSurface,
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

                    Spacer(Modifier.height(12.dp))

                    Divider(
                        modifier = Modifier.fillMaxWidth()
                            .background(MaterialTheme.colorScheme.outlineVariant)
                    )
                }
            }

        }
    }
}