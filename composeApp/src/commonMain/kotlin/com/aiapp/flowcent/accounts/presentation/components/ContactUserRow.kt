package com.aiapp.flowcent.accounts.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.core.presentation.components.SelectionToggle
import com.aiapp.flowcent.core.presentation.components.UserProfileImage

@Composable
fun ContactUserRow(
    user: User, // Replace with your actual data class
    isSelected: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row {
                UserProfileImage(imageUrl = user.imageUrl)

                Spacer(modifier = Modifier.width(12.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.width(300.dp)) {
                            Text(
                                text = user.fullName,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Black,
                            )
                            Text(
                                text = user.phoneNumber,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black,
                                modifier = Modifier.padding(top = 5.dp)
                            )
                        }
                    }

                    Row {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            SelectionToggle(
                                isSelected = isSelected,
                                onToggle = onCheckedChange
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(top = 12.dp, start = 8.dp, end = 8.dp)
            )
        }

    }
}
