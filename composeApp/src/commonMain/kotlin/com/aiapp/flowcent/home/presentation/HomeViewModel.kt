/*
 * Created by Saeedus Salehin on 24/5/25, 9:07 PM.
 */

package com.aiapp.flowcent.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.core.data.model.TransactionDto
import com.aiapp.flowcent.core.data.repository.ExpenseRepository
import com.aiapp.flowcent.core.data.repository.PrefRepository
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.core.presentation.utils.DateTimeUtils.getCurrentDate
import com.aiapp.flowcent.core.domain.utils.Resource
import com.aiapp.flowcent.core.domain.utils.toExpenseItem
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
    private val prefRepository: PrefRepository
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
                    _uiEvent.send(UiEvent.NavigateToAuth)
                }
            }

            UserAction.FirebaseSignOut -> {
                viewModelScope.launch {
                    googleSignOut()
                }
            }

            UserAction.FetchUserUId -> {
                viewModelScope.launch {
                    if (_state.value.uid.isNotEmpty()) {
                        fetchLatestTransactions(_state.value.uid)
                        fetchTotalAmount(_state.value.uid)
                    } else {
                        prefRepository.uid.collect { uidFromDataStore ->
                            _state.update { currentState ->
                                currentState.copy(uid = uidFromDataStore ?: "")
                            }
                            fetchLatestTransactions(uid = uidFromDataStore)
                            fetchTotalAmount(uid = uidFromDataStore)
                        }
                    }
                }
            }

            UserAction.NavigateToProfile -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateToProfile)
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
                    }

                    Resource.Loading -> {}
                    is Resource.Success -> {
                        val transactions = result.data as List<TransactionDto>
                        val expenseList = transactions.map { transaction -> transaction.expenses.map { it.toExpenseItem() } }
                        _state.update {
                            it.copy(
                                latestTransactions = expenseList
                            )
                        }
                        println("Sohan Success in fetching expenses: $expenseList")


                    }
                }
            } catch (error: Exception) {
                println("Sohan Exception  in fetching expenses: ${error.message}")
            }
        }
    }
}
