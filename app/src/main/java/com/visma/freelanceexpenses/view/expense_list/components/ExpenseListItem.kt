package com.visma.freelanceexpenses.view.expense_list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.visma.freelanceexpenses.core.data.currencyList
import com.visma.freelanceexpenses.core.domain.model.Expense
import com.visma.freelanceexpenses.view.expense_list.ExpenseListEvent

@Composable
fun ExpenseListItem(
    expense: Expense,
    onClick: (ExpenseListEvent) -> Unit
) {
    val expenseCurrencySymbol =
        currencyList().find { c -> c.currencyCode == expense.currency }?.currencySymbol ?: ""

    Row(
        modifier = Modifier.fillMaxWidth().padding(all = 5.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = expense.name,
                fontSize = 20.sp
            )
            Text(text = "${expense.total} $expenseCurrencySymbol", fontSize = 12.sp)
        }
        IconButton(onClick = {
            onClick(ExpenseListEvent.DeleteExpense(expense))
        }) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete contact"
            )
        }
    }
}