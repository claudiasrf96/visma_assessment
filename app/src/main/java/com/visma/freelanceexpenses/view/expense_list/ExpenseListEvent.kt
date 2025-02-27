package com.visma.freelanceexpenses.view.expense_list

import com.visma.freelanceexpenses.core.domain.model.Expense

sealed interface ExpenseListEvent {
    data class SortExpenses(val sortType: SortType) : ExpenseListEvent
    data class DeleteExpense(val expense: Expense) : ExpenseListEvent
}