package com.visma.freelanceexpenses.view.expense_list

import com.visma.freelanceexpenses.core.domain.model.Expense

data class ExpenseListState(
    val expenses: List<Expense> = emptyList(),
    val sortType: SortType = SortType.DATE,
)