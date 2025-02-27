package com.visma.freelanceexpenses.view.expense_upsert

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.visma.freelanceexpenses.R
import com.visma.freelanceexpenses.core.data.ExpenseCategory
import com.visma.freelanceexpenses.core.data.currencyList
import com.visma.freelanceexpenses.view.components.AppDropdown
import com.visma.freelanceexpenses.viewmodel.ExpenseUpsertViewModel

@Composable
fun UpsertExpenseRoot(viewModel: ExpenseUpsertViewModel = hiltViewModel(),
                      onEvent: (ExpenseUpsertEvent) -> Unit) {
    val state = viewModel.state.value
    UpsertExpenseScreen(
        state = state,
        onEvent = onEvent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpsertExpenseScreen(
    state: ExpenseUpsertState,
    onEvent: (ExpenseUpsertEvent) -> Unit,
)
{
    val categories = expenseCategoriesToStringList()
    val currencies = currencyList().map { c -> c.currencyCode }

    Surface{
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = {
                    onEvent(ExpenseUpsertEvent.SetName(it))
                },
                label = {
                    Text(text = stringResource(id = R.string.name))
                }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = R.string.currency))
            AppDropdown(itemsList = currencies, onItemClick = {
                    index -> onEvent(ExpenseUpsertEvent.SetCurrency(currencies[index]))
            }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                value = state.total.toString(),
                onValueChange = {
                    val pattern = Regex("^\\d+\$")
                    if (it.isEmpty() || it.matches(pattern)) {
                        val totalVal = it.toDouble()
                        onEvent(ExpenseUpsertEvent.SetTotal(totalVal))
                    }
                },
                label = {
                    Text(text = stringResource(id = R.string.total))
                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = state.date,
                onValueChange = {
                    onEvent(ExpenseUpsertEvent.SetDate(it))
                },
                label = {
                    Text(text = stringResource(id = R.string.date))
                }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = R.string.category))
            AppDropdown(itemsList = categories, onItemClick = {
                    index -> onEvent(ExpenseUpsertEvent.SetCategory(index))

            }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = state.description ?: "",
                onValueChange = {
                    onEvent(ExpenseUpsertEvent.SetCurrency(it))
                },
                label = {
                    Text(text = stringResource(id = R.string.description))
                }, modifier = Modifier.fillMaxWidth()
            )

            // TODO: Add button for image and image preview
        }
    }

}

@Composable
private fun expenseCategoriesToStringList() : List<String>{
    val resultList = arrayListOf<String>()
    val categories = ExpenseCategory.values()
    for (i in categories.indices) {
        val categoryName = when (categories[i]) {
            ExpenseCategory.NO_CATEGORY -> stringResource(R.string.no_category)
            ExpenseCategory.TRANSPORTATION -> stringResource(R.string.transportation)
            ExpenseCategory.OFFICE -> stringResource(R.string.office)
            ExpenseCategory.MEALS -> stringResource(R.string.meals)
            ExpenseCategory.COMMUNICATIONS -> stringResource(R.string.communications)
            ExpenseCategory.BILLS -> stringResource(R.string.bills)
        }
        resultList.add(categoryName)
    }

    return resultList
}
