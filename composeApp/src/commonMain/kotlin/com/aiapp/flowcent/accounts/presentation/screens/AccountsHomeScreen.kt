/*
 * Created by Saeedus Salehin on 15/5/25, 2:55 PM.
 */

package com.aiapp.flowcent.accounts.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aiapp.flowcent.accounts.presentation.AccountState
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UserAction
import com.aiapp.flowcent.accounts.presentation.components.Events
import com.aiapp.flowcent.core.presentation.components.AccountCard
import com.aiapp.flowcent.core.presentation.components.AppButton
import com.aiapp.flowcent.core.presentation.components.ButtonStyle
import com.aiapp.flowcent.core.presentation.components.NoAccountCard
import io.github.aakira.napier.Napier

@Composable
fun AccountsHomeScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel,
    state: AccountState,
    localNavController: NavController,
    globalNavController: NavController
) {

    Events(
        accountViewModel = viewModel,
        globalNavController = globalNavController,
        localNavController = localNavController
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.onAction(UserAction.FetchUserUId)
    }

    Napier.e("Sohan accounts ${state.accounts}")

    Box(
        modifier = modifier.fillMaxSize().padding(20.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (state.isLoading) {
                CircularProgressIndicator()
            } else {
                if (state.accounts.isEmpty()) {
                    NoAccountCard(
                        onClick = { viewModel.onAction(UserAction.ClickAdd) }
                    )
                } else {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Your Accounts",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.titleSmall
                        )

                       AppButton(
                           btnText = "Add More +",
                           textColor = Color.White,
                           bgColor = Color.Black,
                           style = ButtonStyle.PRIMARY,
                           modifier = Modifier.width(180.dp),
                           onClick = { viewModel.onAction(UserAction.ClickAdd) }
                       )
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 16.dp)
                    ) {
                        items(state.accounts) { account ->
                            AccountCard(account = account, onClickItem = {})
                        }
                    }
                }
            }
        }
    }

}