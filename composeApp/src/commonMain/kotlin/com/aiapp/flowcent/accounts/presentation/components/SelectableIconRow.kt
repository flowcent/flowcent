package com.aiapp.flowcent.accounts.presentation.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class SelectableIcon(
    val id: Int,
    val icon: ImageVector
)

@Composable
fun SelectableIconRow(
    icons: List<SelectableIcon>,
    selectedIconId: Int?, // null = none selected
    onIconSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 50.dp,
    cornerRadius: Dp = 12.dp
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        icons.forEach { item ->
            Box(
                modifier = Modifier
                    .size(iconSize)
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(
                        if (item.id == selectedIconId)
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                    .border(
                        if (item.id == selectedIconId) 2.dp else 0.dp,
                        if (item.id == selectedIconId) MaterialTheme.colorScheme.primary else Color.Transparent,
                        if (item.id == selectedIconId) RoundedCornerShape(cornerRadius) else RoundedCornerShape(
                            0.dp
                        )
                    )
                    .clickable { onIconSelected(item.id) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = null,
                    tint = if (item.id == selectedIconId)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
