/*
 * Created by Saeedus Salehin on 15/5/25, 2:48 PM.
 */

package com.aiapp.flowcent.accounts.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.screens.AccountDetailScreen
import com.aiapp.flowcent.accounts.presentation.screens.AccountsHomeScreen
import com.aiapp.flowcent.accounts.presentation.screens.AddAccountScreen
import com.aiapp.flowcent.core.permission.PermissionsViewModel
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AccountsNavGraph(
    modifier: Modifier = Modifier,
    globalNavController: NavHostController,
    startDestination: AccountsNavRoutes
) {
    val localNavController = rememberNavController()
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

    NavHost(
        navController = localNavController,
        startDestination = startDestination.route
    ) {
        addAnimatedComposable(route = AccountsNavRoutes.AccountsHomeScreen.route) {
            AccountsHomeScreen(
                modifier = modifier,
                viewModel = viewModel,
                state = state,
                localNavController = localNavController,
                globalNavController = globalNavController
            )
        }

        addAnimatedComposable(route = AccountsNavRoutes.AddAccountScreen.route) {
            AddAccountScreen(
                modifier = modifier,
                viewModel = viewModel,
                state = state,
                localNavController = localNavController,
                globalNavController = globalNavController,
                permissionVm = permissionVM,
                fcPermissionState = fcPermissionState
            )
        }

        addAnimatedComposable(route = AccountsNavRoutes.AccountDetailScreen.route) {
            AccountDetailScreen(
                modifier = modifier,
                viewModel = viewModel,
                state = state,
                localNavController = localNavController,
                globalNavController = globalNavController,
            )
        }
    }
}