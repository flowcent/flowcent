/*
 * Created by Saeedus Salehin on 24/5/25, 9:07 PM.
 */

package com.aiapp.flowcent.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiapp.flowcent.core.data.repository.ExpenseRepository
import com.aiapp.flowcent.core.domain.model.ExpenseItem
import com.aiapp.flowcent.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onAction(action: UserAction) {
        when (action) {
            is UserAction.FetchLatestTransactions -> {
                fetchLatestTransactions()
            }

            is UserAction.SetSelectedDate -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            selectedDate = action.dateString.toString()
                        )
                    }
                }
            }

        }
    }

    private fun fetchLatestTransactions() {
        viewModelScope.launch {
            try {
                println("Sohan _state.value.selectedDate.toString() ${_state.value.selectedDate.toString()}")
                when (val result =
                    expenseRepository.getDailyExpenses(_state.value.selectedDate.toString())) {
                    is Resource.Error -> {
                        println("Sohan Error in fetching expenses: ${result.message}")
                    }

                    Resource.Loading -> {}
                    is Resource.Success<List<ExpenseItem>> -> {
                        val expenseList = result.data as List<ExpenseItem>
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