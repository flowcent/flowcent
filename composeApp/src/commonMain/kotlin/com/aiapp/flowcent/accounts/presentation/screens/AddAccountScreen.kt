package com.aiapp.flowcent.accounts.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UserAction
import com.aiapp.flowcent.accounts.presentation.components.Events
import com.aiapp.flowcent.auth.data.model.User
import com.aiapp.flowcent.core.permission.FCPermissionState
import com.aiapp.flowcent.core.permission.PermissionsViewModel
import com.aiapp.flowcent.core.presentation.ui.theme.Colors
import dev.icerock.moko.permissions.PermissionState
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.compose_multiplatform
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel,
    state: AccountState,
    localNavController: NavController,
    globalNavController: NavController,
    permissionVm: PermissionsViewModel,
    fcPermissionState: FCPermissionState
) {
    Events(
        accountViewModel = viewModel,
        globalNavController = globalNavController,
        localNavController = localNavController
    )


    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val coroutineScope = rememberCoroutineScope()

    var hasContactPermission: Boolean by remember { mutableStateOf(false) }


    LaunchedEffect(key1 = Unit) {
        permissionVm.provideOrRequestContactPermission()
    }


    LaunchedEffect(key1 = fcPermissionState.contactPermissionState) {
        hasContactPermission = when (fcPermissionState.contactPermissionState) {
            PermissionState.NotDetermined -> false

            PermissionState.NotGranted -> false

            PermissionState.Granted -> true

            PermissionState.Denied -> false

            PermissionState.DeniedAlways -> false

            null -> false
        }
    }

    LaunchedEffect(key1 = state.showSheet) {
        if (state.showSheet) {
            coroutineScope.launch {
                bottomSheetState.show()
            }
        } else {
            coroutineScope.launch {
                bottomSheetState.hide()
            }
        }
    }

    fun handleAddMembers() {
        if (hasContactPermission) {
            viewModel.onAction(UserAction.FetchRegisteredPhoneNumbers)
        } else {
            Napier.e("Sohan Contact permission denied. Please enable it in app settings.")
        }
    }

    fun handleHideBottomSheet() {
        viewModel.onAction(UserAction.UpdateSheetState(false))
    }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Add account screen"
            )

            OutlinedTextField(
                value = state.accountName,
                onValueChange = { newValue ->
                    viewModel.onAction(UserAction.UpdateAccountName(newValue))
                },
                placeholder = { Text("Account Name ") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Colors.Black,
                    cursorColor = Colors.Black,
                    unfocusedContainerColor = Colors.White,
                    focusedContainerColor = Colors.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.acInitialBalance,
                onValueChange = { newValue ->
                    viewModel.onAction(UserAction.UpdateAcInitialBalance(newValue))
                },
                placeholder = { Text("Account Starting Balance ") },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Colors.Black,
                    cursorColor = Colors.Black,
                    unfocusedContainerColor = Colors.White,
                    focusedContainerColor = Colors.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    onClick = { handleAddMembers() },
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color.Black, shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Members",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }

                LazyRow {
                    if (state.selectedUsers.isNotEmpty()) {
                        items(state.selectedUsers) { user ->
                            Box(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .width(80.dp)
                            ) {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // Profile image with box for layering cross icon
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                    ) {
                                        AsyncImage(
                                            model = user.imageUrl,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            placeholder = painterResource(Res.drawable.compose_multiplatform),
                                            modifier = Modifier
                                                .size(64.dp)
                                                .clip(CircleShape)
                                                .border(2.dp, Color.Black, CircleShape)
                                        )

                                        // ❌ Remove icon
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Remove",
                                            tint = Color.Red,
                                            modifier = Modifier
                                                .size(18.dp)
                                                .align(Alignment.TopEnd)
                                                .offset(x = 6.dp, y = (-6).dp)
                                                .clickable {
                                                    viewModel.onAction(
                                                        UserAction.OnRemoveUser(
                                                            user
                                                        )
                                                    )
                                                }
                                                .background(Color.White, CircleShape)
                                                .border(1.dp, Color.Gray, CircleShape)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = user.name,
                                        style = MaterialTheme.typography.labelMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )

                                    Text(
                                        text = user.phoneNumber,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Gray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Column {
            OutlinedButton(
                onClick = { viewModel.onAction(UserAction.CreateAccount)},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Account")
            }
        }
    }

    // Render BottomSheet only when showSheet == true
    if (state.showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                handleHideBottomSheet()
            },
            sheetState = bottomSheetState,
        ) {
            Text(
                "Add Members",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            OutlinedTextField(
                value = "",
                onValueChange = { },
                placeholder = { Text("Search name or number ") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = { },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.White)
            ) {
                if (state.selectedUsers.isNotEmpty()) {
                    items(state.selectedUsers) { user ->
                        Box(
                            modifier = Modifier
                                .padding(12.dp)
                                .width(80.dp)
                        ) {
                            Column(
                                modifier = Modifier.align(Alignment.Center),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Profile image with box for layering cross icon
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                ) {
                                    AsyncImage(
                                        model = user.imageUrl,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        placeholder = painterResource(Res.drawable.compose_multiplatform),
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, Color.Black, CircleShape)
                                    )

                                    // ❌ Remove icon
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = Color.Red,
                                        modifier = Modifier
                                            .size(18.dp)
                                            .align(Alignment.TopEnd)
                                            .offset(x = 6.dp, y = (-6).dp)
                                            .clickable {
                                                viewModel.onAction(
                                                    UserAction.OnRemoveUser(
                                                        user
                                                    )
                                                )
                                            }
                                            .background(Color.White, CircleShape)
                                            .border(1.dp, Color.Gray, CircleShape)
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = user.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = user.phoneNumber,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
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
                                // Circular image
                                AsyncImage(
                                    model = user.imageUrl,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    placeholder = painterResource(Res.drawable.compose_multiplatform),
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .border(
                                            width = 2.dp,
                                            color = Color.Black,
                                            shape = CircleShape
                                        )
                                )

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
                                modifier = Modifier.size(40.dp).padding(end = 10.dp)
                            ) {
                                val isSelected = state.selectedUsers.contains(user)
                                IconToggleButton(
                                    checked = isSelected,
                                    onCheckedChange = { checked ->
                                        viewModel.onAction(
                                            UserAction.OnUserCheckedChange(
                                                user,
                                                checked
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) Color(0xFF4CAF50) else Color.Transparent) // Green when selected
                                        .border(2.dp, Color.Black, CircleShape)
                                ) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Done,
                                            contentDescription = "Selected",
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }


            OutlinedButton(
                onClick = {
                    handleHideBottomSheet()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Done")
            }
        }
    }
}

