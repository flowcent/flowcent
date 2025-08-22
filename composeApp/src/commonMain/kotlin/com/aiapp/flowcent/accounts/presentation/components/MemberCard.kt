package com.aiapp.flowcent.accounts.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.core.presentation.components.NameInitial

@Composable
fun MemberCard(
    name: String,
    initial: String,
    role: String,
    isCurrentUser: Boolean = false,
    showCheckmark: Boolean = false,
    canRemove: Boolean = false,
    onRemove: (() -> Unit)? = null,
    textSize: TextUnit = 14.sp,
    avatarSize: Dp = 40.dp,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Profile Picture (Initials)
        NameInitial(
            text = initial,
            textSize = textSize,
            size = avatarSize
        )

        Spacer(Modifier.width(12.dp))

        // Name + Role
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = if (isCurrentUser) "You" else name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = role,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // ✅ Checkmark (optional)
        if (showCheckmark) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Checked",
                tint = Color(0xFF4CAF50), // Green 500
                modifier = Modifier.size(20.dp)
            )
        }

        // ❌ Remove (optional)
        if (canRemove && onRemove != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove Member",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onRemove() }
            )
        }
    }
}

