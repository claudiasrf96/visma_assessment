package com.visma.freelanceexpenses.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.visma.freelanceexpenses.core.domain.model.Expense
import com.visma.freelanceexpenses.core.domain.repository.ExpenseRepository
import com.visma.freelanceexpenses.view.expense_upsert.ExpenseUpsertEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ExpenseUpsertViewModel @Inject constructor(private val repository: ExpenseRepository,
                                                 savedStateHandle: SavedStateHandle
)
    : ViewModel() {

    private val _name = mutableStateOf("")
    private val _imageLocation = mutableStateOf<String?>(null)
    private val _description = mutableStateOf<String?>(null)
    private val _currency = mutableStateOf("EUR")
    private val _total = mutableStateOf(0.0)
    private val _date = mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_DATE))
    private val _category = mutableStateOf(0)

    val name : State<String> = _name
    val imageLocation : State<String?> = _imageLocation
    val description : State<String?> = _description
    val currency : State<String> = _currency
    val total : State<Double> = _total
    val date : State<String> = _date
    val category : State<Int> = _category

    private var currentExpenseId: Int? = null

    init {
        savedStateHandle.get<Int>("expenseId")?.let { expenseId ->
            if(expenseId != -1) {
                viewModelScope.launch {
                    repository.getExpensesById(expenseId)?.also { expense ->
                        currentExpenseId = expense.id
                        _category.value = expense.category
                        _currency.value = expense.currency
                        _date.value = expense.date
                        _description.value = expense.description
                        _imageLocation.value = expense.imageLocation
                        _name.value = expense.name
                        _total.value = expense.total
                    }
                }
            }
        }
    }

    fun onEvent(event: ExpenseUpsertEvent) {
        when(event){
            ExpenseUpsertEvent.SaveExpense -> {
                viewModelScope.launch {
                    val name = _name.value
                    val currency = currency.value
                    val total = total.value

                    if (name.isBlank() || currency.isBlank() || total <= 0.01) {
                        return@launch
                    }
                    val expense = if(currentExpenseId == null)  Expense(
                        name,
                        imageLocation.value,
                        currency,
                        total,
                        description.value,
                        date.value,
                        category.value)
                    else Expense(
                        name,
                        imageLocation.value,
                        currency,
                        total,
                        description.value,
                        date.value,
                        category.value, currentExpenseId!!)

                    repository.upsertExpense(expense)

                }
            }
            is ExpenseUpsertEvent.SetCategory -> {
                _category.value = event.category
            }
            is ExpenseUpsertEvent.SetCurrency -> {
                _currency.value = event.currency
            }
            is ExpenseUpsertEvent.SetDate -> {
                _date.value = event.date
            }
            is ExpenseUpsertEvent.SetDescription -> {
                _description.value = event.description
            }
            is ExpenseUpsertEvent.SetImageLocation -> {
                _imageLocation.value = event.imageLocation
            }
            is ExpenseUpsertEvent.SetName -> {
                _name.value = event.name
            }
            is ExpenseUpsertEvent.SetTotal -> {
                _total.value = event.total
            }
        }
    }
}