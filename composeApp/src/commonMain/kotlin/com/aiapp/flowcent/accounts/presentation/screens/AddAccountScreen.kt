package com.aiapp.flowcent.accounts.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.accounts.domain.model.MemberRole
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UserAction
import com.aiapp.flowcent.accounts.presentation.components.AddMembersSheetContent
import com.aiapp.flowcent.accounts.presentation.components.MemberCard
import com.aiapp.flowcent.accounts.presentation.components.SelectableIconRow
import com.aiapp.flowcent.accounts.presentation.getAccountIcons
import com.aiapp.flowcent.core.presentation.components.AppButton
import com.aiapp.flowcent.core.presentation.components.LabeledInputField
import com.aiapp.flowcent.core.presentation.extension.dottedBorder
import com.aiapp.flowcent.core.presentation.permission.FCPermissionState
import com.aiapp.flowcent.core.presentation.permission.PermissionsViewModel
import dev.icerock.moko.permissions.PermissionState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel,
    state: AccountState,
    permissionVm: PermissionsViewModel,
    fcPermissionState: FCPermissionState
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    var hasContactPermission: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        permissionVm.provideOrRequestContactPermission()
        viewModel.onAction(UserAction.FetchUserUId)
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
        modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f)
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Big Icon Circle
            Box(
                modifier = Modifier.size(80.dp).background(
                    color = MaterialTheme.colorScheme.primary, shape = CircleShape
                ), contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Groups,
                    contentDescription = "Group Icon",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(40.dp)
                )

            }

            Spacer(Modifier.height(16.dp))

            // Title
            Text(
                text = "Create Shared Account",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Share expenses with family, friends, or roommates.\nTrack spending together and split costs easily.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            // Account Name

            LabeledInputField(
                label = "Account Name",
                placeholder = "e.g., Family Expenses, Roommates",
                value = state.accountName,
                onValueChange = { accountName ->
                    viewModel.onAction(UserAction.UpdateAccountName(accountName))
                }

            )

            Spacer(Modifier.height(24.dp))

            // Opening Balance
            LabeledInputField(
                label = "Initial Balance",
                placeholder = "What's your initial balance?",
                isNumeric = true,
                value = if (state.acInitialBalance > 0.0) {
                    if (state.acInitialBalance % 1.0 == 0.0) {
                        state.acInitialBalance.toInt().toString() // Remove .0 for whole numbers
                    } else {
                        state.acInitialBalance.toString()
                    }
                } else "",
                onValueChange = { initialBalance ->
                    viewModel.onAction(UserAction.UpdateAcInitialBalance(initialBalance.toDouble()))
                },
            )

            Spacer(Modifier.height(24.dp))

            // Description
            LabeledInputField(
                label = "Description (Optional)",
                placeholder = "What's this account for?",
                value = state.accountDescription,
                onValueChange = { description ->
                    viewModel.onAction(UserAction.UpdateAccountDescription(description))
                },
                height = 100.dp,
                singleLine = false,
                maxLines = 8
            )

            Spacer(Modifier.height(24.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Title
                Text(
                    text = "Add Initial Members",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.Start)
                )

                state.selectedUsers.forEach { user ->
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        if (user.uid == state.uid) {
                            MemberCard(
                                name = "You",
                                initial = user.localUserName,
                                role = MemberRole.ADMIN.displayName,
                                isCurrentUser = true,
                                showCheckmark = true
                            )
                        } else {
                            MemberCard(
                                name = user.localUserName,
                                initial = user.localUserName,
                                role = MemberRole.MEMBER.displayName,
                                canRemove = true,
                                onRemove = { viewModel.onAction(UserAction.OnRemoveMember(user)) }
                            )
                        }
                    }
                }


                // Invite Members Button
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).dottedBorder(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        strokeWidth = 2.dp,
                        cornerRadius = 12.dp,
                        dotInterval = 6.dp
                    ).clickable { handleAddMembers() }.padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Invite members",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Icon Picker
            Text(
                text = "Choose Icon",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(Modifier.height(8.dp))

            SelectableIconRow(
                icons = getAccountIcons(),
                selectedIconId = state.selectedIconId,
                onIconSelected = { id ->
                    viewModel.onAction(UserAction.SelectAccountIcon(id))
                }
            )
            Spacer(Modifier.height(8.dp))
        }


        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Divider(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp)
            ) {
                AppButton(
                    text = "Create Account",
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        viewModel.onAction(UserAction.CreateAccount)
                    })

                Spacer(Modifier.height(16.dp))

                Text(
                    text = "By Creating an account, you agree to our Terms of Service and Privacy Policy",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(Modifier.height(16.dp))
            }
        }

        if (state.showSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    handleHideBottomSheet()
                },
                sheetState = bottomSheetState,
                modifier = Modifier.padding(top = 200.dp),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                AddMembersSheetContent(
                    state = state, viewModel = viewModel, onDoneClick = {
                        handleHideBottomSheet()
                    })
            }
        }
    }
}