/*
 * Created by Saeedus Salehin on 15/5/25, 2:48 PM.
 */

package com.aiapp.flowcent.accounts.presentation.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.aiapp.flowcent.accounts.presentation.AccountViewModel
import com.aiapp.flowcent.accounts.presentation.screens.AccountDetailScreen
import com.aiapp.flowcent.accounts.presentation.screens.AccountsHomeScreen
import com.aiapp.flowcent.accounts.presentation.screens.AddAccountScreen
import com.aiapp.flowcent.accounts.presentation.screens.BaseScreen
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.navigation.addAnimatedComposable
import com.aiapp.flowcent.core.presentation.permission.PermissionsViewModel
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.addAccountsGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    navigation(
        route = AppNavRoutes.Accounts.route,
        startDestination = AccountsNavRoutes.AccountsHomeScreen.route
    ) {
        addAnimatedComposable(route = AccountsNavRoutes.AccountsHomeScreen.route) { navBackStackEntry ->
            val accountNavGraphEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppNavRoutes.Accounts.route)
            }
            val viewModel = koinViewModel<AccountViewModel>(
                viewModelStoreOwner = accountNavGraphEntry
            )

            BaseScreen(
                navController = navController,
                viewModel = viewModel,
                modifier = modifier,
            ) { modifier, acViewModel, state ->
                AccountsHomeScreen(
                    modifier = modifier,
                    viewModel = acViewModel,
                    state = state
                )
            }

        }

        addAnimatedComposable(route = AccountsNavRoutes.AddAccountScreen.route) { navBackStackEntry ->
            val accountNavGraphEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppNavRoutes.Accounts.route)
            }
            val viewModel = koinViewModel<AccountViewModel>(
                viewModelStoreOwner = accountNavGraphEntry
            )

            val factory = rememberPermissionsControllerFactory()
            val controller = remember(factory) {
                factory.createPermissionsController()
            }

            BindEffect(controller)

            val permissionVM = androidx.lifecycle.viewmodel.compose.viewModel {
                PermissionsViewModel(controller)
            }

            val fcPermissionState by permissionVM.state.collectAsState()

            BaseScreen(
                navController = navController,
                viewModel = viewModel,
                modifier = modifier,
            ) { modifier, acViewModel, state ->
                AddAccountScreen(
                    modifier = modifier,
                    viewModel = acViewModel,
                    state = state,
                    permissionVm = permissionVM,
                    fcPermissionState = fcPermissionState
                )
            }
        }

        addAnimatedComposable(route = AccountsNavRoutes.AccountDetailScreen.route) { navBackStackEntry ->
            val accountNavGraphEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(AppNavRoutes.Accounts.route)
            }
            val viewModel = koinViewModel<AccountViewModel>(
                viewModelStoreOwner = accountNavGraphEntry
            )

            BaseScreen(
                navController = navController,
                viewModel = viewModel,
                modifier = modifier,
            ) { modifier, acViewModel, state ->
                AccountDetailScreen(
                    modifier = modifier,
                    viewModel = acViewModel,
                    state = state
                )
            }
        }
    }
}