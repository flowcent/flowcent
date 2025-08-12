package com.aiapp.flowcent.accounts.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.core.presentation.components.NameInitial
import com.aiapp.flowcent.core.presentation.components.SelectionToggle
import com.aiapp.flowcent.core.presentation.components.UserProfileImage
import com.aiapp.flowcent.core.presentation.ui.theme.Colors

@Composable
fun ContactUserRow(
    fullName: String = "",
    phoneNumber: String = "",
    imageUrl: String = "",
    showSelection: Boolean = false,
    isSelected: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = modifier.weight(1f)
        ) {
            if (imageUrl.isNotEmpty()) {
                UserProfileImage(imageUrl = imageUrl)
            } else {
                NameInitial(fullName)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.width(300.dp)) {
                Text(
                    text = fullName,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                )
                Text(
                    text = phoneNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
        }

        Row {
            if (showSelection) {
                Checkbox(
                    checked = isSelected,
                    onCheckedChange = { onCheckedChange(it) },
                    colors = CheckboxDefaults.colors(checkedColor = Colors.IncomeColor)
                )
            } else {
                Text(
                    text = "Invite",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Colors.IncomeColor,
                    modifier = Modifier.clickable { }
                )
            }
        }

    }
}
