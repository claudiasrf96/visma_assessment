package com.visma.freelanceexpenses.view.expense_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.visma.freelanceexpenses.core.domain.model.Expense
import com.visma.freelanceexpenses.view.expense_list.ExpenseListEvent


@Composable
fun ExpenseList(
    expenses: LazyPagingItems<Expense>,
    onExpenseClick: (Expense) -> Unit,
    onDeleteClick: (ExpenseListEvent) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            count = expenses.itemCount,
            key = expenses.itemKey { expense -> expense.id },
            contentType = expenses.itemContentType {"Expenses"}
        ) { index ->
            val expense = expenses[index]
            if (expense != null) {
                ExpenseListItem(
                    expense = expense,
                    onClickExpense = { onExpenseClick(expense) },
                    onClickDelete = onDeleteClick
                )
            }
        }
    }
}