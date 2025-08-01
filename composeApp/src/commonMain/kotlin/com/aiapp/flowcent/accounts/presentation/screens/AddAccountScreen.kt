package com.aiapp.flowcent.accounts.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UserAction
import com.aiapp.flowcent.accounts.presentation.components.AddMembersSheetContent
import com.aiapp.flowcent.accounts.presentation.components.Events
import com.aiapp.flowcent.accounts.presentation.components.SelectedUserCard
import com.aiapp.flowcent.core.permission.FCPermissionState
import com.aiapp.flowcent.core.permission.PermissionsViewModel
import com.aiapp.flowcent.core.presentation.components.AppButton
import com.aiapp.flowcent.core.presentation.components.AppIconButton
import com.aiapp.flowcent.core.presentation.components.AppTextField
import com.aiapp.flowcent.core.presentation.components.IconCard
import com.aiapp.flowcent.core.presentation.components.NumericInputField
import dev.icerock.moko.permissions.PermissionState
import io.github.aakira.napier.Napier
import kotlinx.coroutines.launch

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
                modifier = Modifier.padding(top = 12.dp)
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

            Text(
                text = "Want to open a group account?",
                style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 12.dp, start = 8.dp, end = 8.dp)
            )

            Text(
                text = "Why not adding some friends to your account?",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
            )

            Text(
                text = "This will be fun!!",
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp, start = 8.dp, end = 8.dp)
            )

            IconCard(
                icon = Icons.Default.PersonAdd,
                text = "Add some members",
                onClick = {
                    handleAddMembers()
                },
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Column {
            AppButton(
                btnText = "Create Your Account",
                onClick = {
                    viewModel.onAction(UserAction.CreateAccount)
                },
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
            modifier = Modifier.padding(top = 200.dp)
        ) {
            AddMembersSheetContent(
                state = state,
                viewModel = viewModel,
                onDoneClick = {
                    handleHideBottomSheet()
                }
            )
        }
    }
}

