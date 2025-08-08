package com.aiapp.flowcent.chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.chat.domain.model.AccountSelectionType

@Composable
fun AccountSelectionToggle(
    modifier: Modifier = Modifier,
    selectionType: AccountSelectionType,
    onSelectionChanged: (AccountSelectionType) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp)) // Light background
    ) {
        SelectionItem(
            icon = Icons.Default.AccountBalanceWallet,
            title = "Personal",
            isToggled = selectionType == AccountSelectionType.PERSONAL,
            onToggle = { onSelectionChanged(AccountSelectionType.PERSONAL) }
        )

        SelectionItem(
            icon = Icons.Default.Refresh,
            title = "Shared",
            isToggled = selectionType == AccountSelectionType.SHARED,
            onToggle = { onSelectionChanged(AccountSelectionType.SHARED) }
        )
    }
}

@Composable
fun SelectionItem(
    icon: ImageVector,
    title: String,
    isToggled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF5F6368),
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyLarge)
            }
        }
        Switch(
            checked = isToggled,
            onCheckedChange = { onToggle(true) },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White
            )
        )
    }
}
