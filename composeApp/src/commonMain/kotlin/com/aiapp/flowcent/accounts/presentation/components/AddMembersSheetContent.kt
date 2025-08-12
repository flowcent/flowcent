package com.aiapp.flowcent.accounts.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UserAction
import com.aiapp.flowcent.core.presentation.components.AppTextField

@Composable
fun AddMembersSheetContent(
    state: AccountState,
    viewModel: AccountViewModel,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp, vertical = 12.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                "Cancel",
                style = MaterialTheme.typography.bodyLarge,
            )

            Text(
                "Add Members",
                style = MaterialTheme.typography.titleMedium,
            )

            Text(
                "Next",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.clickable { onDoneClick() }
            )
        }

        AppTextField(
            value = "", // You can pass this as a parameter if needed
            onValueChange = { /* Handle search */ },
            placeholder = "Search name or number",
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
        )

        LazyRow {
            if (state.selectedUsers.isNotEmpty()) {
                items(state.selectedUsers) { user ->
                    SelectedUserCard(
                        user = user,
                        onRemoveClick = { viewModel.onAction(UserAction.OnRemoveUser(user)) }
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
        ) {


            item() {
                if (state.matchingUsers.isNotEmpty()) {
                    Text(
                        "Your potential group members",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                    )
                }

            }

            items(state.matchingUsers.toList()) { user ->
                ContactUserRow(
                    fullName = user.localUserName,
                    phoneNumber = user.phoneNumber,
                    imageUrl = user.imageUrl,
                    showSelection = true,
                    isSelected = state.selectedUsers.contains(user),
                    onCheckedChange = { isChecked ->
                        viewModel.onAction(
                            UserAction.OnUserCheckedChange(user, isChecked)
                        )
                    }
                )
            }

            item {
                Text(
                    "Invite Others",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp)
                )
            }


            items(state.deviceContacts.toList()) { contact ->
                ContactUserRow(
                    fullName = contact.name ?: "",
                    phoneNumber = contact.phoneNumber,
                )
            }
        }
    }
}

