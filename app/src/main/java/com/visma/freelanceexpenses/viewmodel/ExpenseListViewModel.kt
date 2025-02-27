package com.visma.freelanceexpenses.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visma.freelanceexpenses.core.domain.model.Expense
import com.visma.freelanceexpenses.core.domain.repository.ExpenseRepository
import com.visma.freelanceexpenses.view.expense_list.ExpenseListEvent
import com.visma.freelanceexpenses.view.expense_list.ExpenseListState
import com.visma.freelanceexpenses.view.expense_list.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseListViewModel @Inject constructor(private val repository: ExpenseRepository)
    : ViewModel() {

    private val _sortType = MutableStateFlow(SortType.DATE)
    private val _state = mutableStateOf(ExpenseListState())
    val state : State<ExpenseListState> = _state

    private var getExpensesJob: Job? = null

    init {
        getExpenses(_sortType.value)
    }

    fun onEvent(event: ExpenseListEvent) {
        when(event){
            is ExpenseListEvent.DeleteExpense -> {
                viewModelScope.launch {
                    repository.deleteExpense(event.expense)
                }
            }
            is ExpenseListEvent.SortExpenses -> {
                if (state.value.sortType == event.sortType
                ) {
                    return
                }
                getExpenses(event.sortType)
            }
        }
    }

    private fun getExpenses(sortType: SortType) {
        getExpensesJob?.cancel()
        getExpensesJob = mapExpenseList(sortType)
            .onEach { expenses ->
                _state.value = state.value.copy(
                    expenses = expenses,
                    sortType = sortType
                )
            }
            .launchIn(viewModelScope)

        /*val expenses = listOf(
            Expense(name = "My expense", currency = "EUR", total = 250.5, date = "2025-01-25", category = 0, id = 1),
            Expense(name = "My second expense", currency = "EUR", total = 100.0, date = "2025-01-30", category = 3, id = 2)
        )

        _state.value = state.value.copy(
            expenses = expenses,
            sortType = sortType
        )*/
    }

    private fun mapExpenseList(sortType: SortType) : Flow<List<Expense>> {
        return repository.getExpenses().map { expenses ->
            when(sortType){
                SortType.DATE -> expenses.sortedBy { it.date }
                SortType.NAME -> expenses.sortedBy { it.name }
                SortType.TOTAL -> expenses.sortedBy { it.total }
            }
        }
    }
}