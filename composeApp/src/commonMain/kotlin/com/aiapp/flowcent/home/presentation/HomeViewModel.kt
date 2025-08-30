/*
 * Created by Saeedus Salehin on 24/5/25, 9:07 PM.
 */

package com.aiapp.flowcent.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.auth.data.repository.AuthRepository
import com.aiapp.flowcent.core.data.model.TransactionDto
import com.aiapp.flowcent.core.data.repository.ExpenseRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.domain.utils.Resource
import com.aiapp.flowcent.core.domain.utils.toExpenseItem
import com.aiapp.flowcent.core.presentation.navigation.AppNavRoutes
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentDate
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import io.github.aakira.napier.Napier
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val expenseRepository: ExpenseRepository,
    private val prefRepository: PrefRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        _state.update {
            it.copy(
                selectedDate = getCurrentDate().toString()
            )
        }
    }

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.SetSelectedDate -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            selectedDate = action.dateString.toString()
                        )
                    }
                }
            }

            UserAction.NavigateToAuth -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AppNavRoutes.Auth.route))
                }
            }

            UserAction.FirebaseSignOut -> {
                viewModelScope.launch {
                    googleSignOut()
                }
            }

            UserAction.FetchUserUId -> {
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    if (_state.value.uid.isNotEmpty()) {
                        fetchUserProfile(state.value.uid)
                        fetchLatestTransactions(_state.value.uid)
                        fetchTotalAmount(_state.value.uid)
                    } else {
                        prefRepository.uid.collect { uidFromDataStore ->
                            _state.update { currentState ->
                                currentState.copy(uid = uidFromDataStore ?: "")
                            }
                            fetchUserProfile(uid = uidFromDataStore)
                            fetchLatestTransactions(uid = uidFromDataStore)
                            fetchTotalAmount(uid = uidFromDataStore)
                        }
                    }
                }
            }

            UserAction.NavigateToProfile -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AppNavRoutes.Profile.route))
                }
            }

            UserAction.NavigateToInsights -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.Navigate(AppNavRoutes.Insights.route))
                }
            }
        }
    }

    private suspend fun fetchUserProfile(uid: String?) {
        if (uid.isNullOrEmpty()) {
            Napier.e("Sohan 404 No User Found")
            return
        }
        when (val result = authRepository.fetchUserProfile(uid)) {
            is Resource.Error -> {
                println("Sohan Error in fetching user profile: ${result.message}")
            }

            Resource.Loading -> {}
            is Resource.Success -> {
                _state.update {
                    it.copy(
                        user = result.data,
                        userInitialBalance = result.data?.initialBalance ?: 0.0,
                        userSavingTarget = result.data?.savingTarget ?: 0.0
                    )
                }
            }
        }

    }

    private suspend fun googleSignOut() {
        println("Sohan Firebase.auth.currentUser ${Firebase.auth.currentUser}")
        if (Firebase.auth.currentUser != null) {
            Firebase.auth.signOut()
        }
    }

    private fun fetchTotalAmount(uid: String?) {
        if (uid.isNullOrEmpty()) {
            Napier.e("Sohan 404 No user found")
            return
        }
        viewModelScope.launch {
            when (val result = expenseRepository.totalAmount(uid)) {
                is Resource.Error -> {
                    println("Sohan Error in fetching total amount: ${result.message}")
                }

                is Resource.Success -> {
                    println("Sohan Success in fetching total amount: ${result.data}")
                    _state.update {
                        it.copy(
                            userTotalSpent = result.data ?: 0.0
                        )
                    }
                }

                Resource.Loading -> {}

            }
        }
    }

    private fun fetchLatestTransactions(uid: String?) {
        println("Sohan fetchLatestTransactions")
        if (uid.isNullOrEmpty()) {
            Napier.e("Sohan 404 No User Found")
            return
        }
        viewModelScope.launch {
            try {
                when (val result =
                    expenseRepository.getDailyExpenses(
                        uid,
                        _state.value.selectedDate.toString()
                    )) {
                    is Resource.Error -> {
                        println("Sohan Error in fetching expenses: ${result.message}")
                        _state.update { it.copy(isLoading = false) }
                    }

                    Resource.Loading -> {}
                    is Resource.Success -> {
                        val transactions = result.data as List<TransactionDto>
                        val expenseList =
                            transactions.map { transaction -> transaction.expenses.map { it.toExpenseItem() } }
                        _state.update {
                            it.copy(
                                latestTransactions = expenseList,
                                isLoading = false
                            )
                        }
                        println("Sohan Success in fetching expenses: $expenseList")


                    }
                }
            } catch (error: Exception) {
                println("Sohan Exception  in fetching expenses: ${error.message}")
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}