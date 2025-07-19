package com.aiapp.flowcent.accounts.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.components.Events
import com.aiapp.flowcent.core.presentation.ui.theme.Colors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAccountScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel,
    state: AccountState,
    localNavController: NavController,
    globalNavController: NavController
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var showSheet by remember { mutableStateOf(false) }  // NEW

    Events(
        accountViewModel = viewModel,
        globalNavController = globalNavController,
        localNavController = localNavController
    )

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
                value = "",
                onValueChange = { newValue -> },
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
                value = "",
                onValueChange = { newValue -> },
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

            OutlinedButton(
                onClick = {
                    showSheet = true
                    coroutineScope.launch {
                        bottomSheetState.show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Text("Add Members >")
            }
        }

        Column {
            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Create Account")
            }
        }
    }

    // Render BottomSheet only when showSheet == true
    if (showSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch {
                    bottomSheetState.hide()
                    showSheet = false
                }
            },
            sheetState = bottomSheetState,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Add Members", style = MaterialTheme.typography.titleLarge)

                OutlinedTextField(
                    value = "",
                    onValueChange = { },
                    placeholder = { Text("Search by username") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    trailingIcon = { }
                )

                OutlinedButton(
                    onClick = {
                        coroutineScope.launch {
                            bottomSheetState.hide()
                            showSheet = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Done")
                }
            }
        }
    }
}
