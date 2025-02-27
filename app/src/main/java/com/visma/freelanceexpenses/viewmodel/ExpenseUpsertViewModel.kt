package com.visma.freelanceexpenses.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visma.freelanceexpenses.core.domain.model.Expense
import com.visma.freelanceexpenses.core.domain.ExpenseDao
import com.visma.freelanceexpenses.core.domain.repository.ExpenseRepository
import com.visma.freelanceexpenses.view.expense_upsert.ExpenseUpsertEvent
import com.visma.freelanceexpenses.view.expense_upsert.ExpenseUpsertState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseUpsertViewModel @Inject constructor(private val repository: ExpenseRepository)
    : ViewModel() {

    private val _state = mutableStateOf(ExpenseUpsertState())

    val state : State<ExpenseUpsertState> = _state

    fun onEvent(event: ExpenseUpsertEvent) {
        when(event){
            ExpenseUpsertEvent.SaveExpense -> {
                viewModelScope.launch {
                    val name = _state.value.name
                    val currency = _state.value.currency
                    val total = _state.value.total

                    if (name.isBlank() || currency.isBlank() || total <= 0.0) {
                        return@launch
                    }

                    val expense = Expense(
                        name,
                        _state.value.imageLocation,
                        currency,
                        total,
                        _state.value.description,
                        _state.value.date,
                        _state.value.category)
                    repository.upsertExpense(expense)
                    _state.value = _state.value.copy(expense = expense)
                }
            }
            is ExpenseUpsertEvent.SetCategory -> {
                _state.value = _state.value.copy(category = event.category)
            }
            is ExpenseUpsertEvent.SetCurrency -> {
                _state.value = _state.value.copy(currency = event.currency)
            }
            is ExpenseUpsertEvent.SetDate -> {
                _state.value = _state.value.copy(date = event.date)
            }
            is ExpenseUpsertEvent.SetDescription -> {
                _state.value = _state.value.copy(description = event.description)
            }
            is ExpenseUpsertEvent.SetImageLocation -> {
                _state.value = _state.value.copy(imageLocation = event.imageLocation)
            }
            is ExpenseUpsertEvent.SetName -> {
                _state.value = _state.value.copy(name = event.name)
            }
            is ExpenseUpsertEvent.SetTotal -> {
                _state.value = _state.value.copy(total = event.total)
            }
        }
    }
}