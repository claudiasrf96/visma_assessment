package com.visma.freelanceexpenses.view.expense_list

import androidx.paging.PagingData
import com.visma.freelanceexpenses.core.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class ExpenseListState(
    val expenses: Flow<PagingData<Expense>> = emptyFlow(),
    val sortType: SortType = SortType.DATE
)