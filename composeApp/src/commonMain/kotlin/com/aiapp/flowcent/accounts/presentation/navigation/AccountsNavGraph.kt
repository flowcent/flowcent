/*
 * Created by Saeedus Salehin on 15/5/25, 2:48 PM.
 */

package com.aiapp.flowcent.accounts.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.UiEvent
import com.aiapp.flowcent.accounts.presentation.screens.AccountDetailScreen
import com.aiapp.flowcent.accounts.presentation.screens.AccountsHomeScreen
import com.aiapp.flowcent.accounts.presentation.screens.AddAccountScreen
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.permission.PermissionsViewModel
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.addAccountsGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    addAnimatedComposable(route = AppNavRoutes.Accounts.route) {
        val viewModel = koinViewModel<AccountViewModel>()
        val state by viewModel.state.collectAsState()

        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

        BindEffect(controller)

        val permissionVM = androidx.lifecycle.viewmodel.compose.viewModel {
            PermissionsViewModel(controller)
        }

        val fcPermissionState by permissionVM.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    UiEvent.ClickAdd -> {
                        navController.navigate(AccountsNavRoutes.AddAccountScreen.route)
                    }

                    UiEvent.NavigateToAccountDetail -> {
                        navController.navigate(AccountsNavRoutes.AccountDetailScreen.route)
                    }

                    UiEvent.NavigateToChat -> {
                        navController.navigate(AppNavRoutes.Chat.route)
                    }

                    UiEvent.NavigateToAccountHome -> {
                        navController.navigate(AccountsNavRoutes.AccountsHomeScreen.route)
                    }
                }
            }
        }

        AccountsHomeScreen(
            modifier = modifier,
            viewModel = viewModel,
            state = state
        )
    }

    addAnimatedComposable(route = AccountsNavRoutes.AddAccountScreen.route) {
        val viewModel = koinViewModel<AccountViewModel>()
        val state by viewModel.state.collectAsState()
        val factory = rememberPermissionsControllerFactory()
        val controller = remember(factory) {
            factory.createPermissionsController()
        }

        BindEffect(controller)

        val permissionVM = androidx.lifecycle.viewmodel.compose.viewModel {
            PermissionsViewModel(controller)
        }

        val fcPermissionState by permissionVM.state.collectAsState()

        AddAccountScreen(
            modifier = modifier,
            viewModel = viewModel,
            state = state,
            permissionVm = permissionVM,
            fcPermissionState = fcPermissionState
        )
    }

    addAnimatedComposable(route = AccountsNavRoutes.AccountDetailScreen.route) {
        val viewModel = koinViewModel<AccountViewModel>()
        val state by viewModel.state.collectAsState()
        AccountDetailScreen(
            modifier = modifier,
            viewModel = viewModel,
            state = state
        )
    }
}