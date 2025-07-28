package com.aiapp.flowcent.accounts.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UserAction
import com.aiapp.flowcent.core.presentation.components.SelectionToggle
import com.aiapp.flowcent.core.presentation.components.UserProfileImage

@Composable
fun AddMembersSheetContent(
    state: AccountState,
    viewModel: AccountViewModel,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            "Add Members",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        OutlinedTextField(
            value = "", // You can pass this as a parameter if needed
            onValueChange = { /* Handle search */ },
            placeholder = { Text("Search name or number") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (state.selectedUsers.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White)
            ) {
                if (state.selectedUsers.isNotEmpty()) {
                    items(state.selectedUsers) { user ->
                        SelectedUserCard(
                            user = user,
                            onRemoveClick = { viewModel.onAction(UserAction.OnRemoveUser(user)) }
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White)
        ) {
            state.matchingUsers?.forEach { user ->
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row {
                            UserProfileImage(imageUrl = user.imageUrl)

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = user.name,
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

                        Row(
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 10.dp)
                        ) {
                            val isSelected = state.selectedUsers.contains(user)
                            SelectionToggle(
                                isSelected = isSelected,
                                onToggle = { checked ->
                                    viewModel.onAction(
                                        UserAction.OnUserCheckedChange(
                                            user,
                                            checked
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

        OutlinedButton(
            onClick = onDoneClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Done")
        }
    }
}
