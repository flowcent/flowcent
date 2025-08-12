package com.aiapp.flowcent.accounts.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UserAction
import com.aiapp.flowcent.accounts.presentation.components.AddMembersSheetContent
import com.aiapp.flowcent.accounts.presentation.components.SelectedUserCard
import com.aiapp.flowcent.core.presentation.components.AppButton
import com.aiapp.flowcent.core.presentation.components.AppCustomButton
import com.aiapp.flowcent.core.presentation.components.AppTextField
import com.aiapp.flowcent.core.presentation.components.IconCard
import com.aiapp.flowcent.core.presentation.components.NumericInputField
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
        modifier = modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
        ) {

            AppTextField(
                value = state.accountName,
                onValueChange = { newValue ->
                    viewModel.onAction(UserAction.UpdateAccountName(newValue))
                },
                placeholder = "Enter a name to identify this account easily.",
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            )


            NumericInputField(
                value = if (state.acInitialBalance > 0.0) {
                    if (state.acInitialBalance % 1.0 == 0.0) {
                        state.acInitialBalance.toInt().toString() // Remove .0 for whole numbers
                    } else {
                        state.acInitialBalance.toString()
                    }
                } else "",
                onValueChange = { newValue ->
                    viewModel.onAction(UserAction.UpdateAcInitialBalance(newValue.toDouble()))
                },
                placeholder = "Add an opening balance to the account.",
                modifier = Modifier.padding(top = 12.dp)
            )

            IconCard(
                icon = Icons.Default.PersonAdd, text = "Add some members", onClick = {
                    handleAddMembers()
                }, modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.padding(12.dp))

            LazyRow {
                if (state.selectedUsers.isNotEmpty()) {
                    items(state.selectedUsers) { user ->
                        SelectedUserCard(
                            user = user,
                            onRemoveClick = { viewModel.onAction(UserAction.OnRemoveUser(user)) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.padding(12.dp))
                    }
                }
            }
        }

        Column {
            AppButton(
                text = "Next",
                textColor = Color.White,
                backgroundColor = Color.Black,
                onClick = {
                    viewModel.onAction(UserAction.CreateAccount)
                },
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth()
            )
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
                state = state,
                viewModel = viewModel,
                onDoneClick = {
                    handleHideBottomSheet()
                })
        }
    }
}

