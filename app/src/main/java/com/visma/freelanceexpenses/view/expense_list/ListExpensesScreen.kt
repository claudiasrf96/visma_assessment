package com.visma.freelanceexpenses.view.expense_list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.visma.freelanceexpenses.R
import com.visma.freelanceexpenses.core.domain.model.Expense
import com.visma.freelanceexpenses.ui.theme.DarkBlue
import com.visma.freelanceexpenses.ui.theme.DesertWhite
import com.visma.freelanceexpenses.ui.theme.SandYellow
import com.visma.freelanceexpenses.view.components.AppDropdown
import com.visma.freelanceexpenses.view.expense_list.components.ExpenseList
import com.visma.freelanceexpenses.viewmodel.ExpenseListViewModel

@Composable
fun ListExpensesScreenRoot(viewModel: ExpenseListViewModel = hiltViewModel(),
                           onExpenseAddClick: () -> Unit,
                           onExpenseClick: (Expense) -> Unit,
                           onEvent: (ExpenseListEvent) -> Unit) {
    val state = viewModel.state.value
    ListExpensesScreen(
        state = state,
        onAddClick = { onExpenseAddClick() },
        onExpenseClick = onExpenseClick,
        onEvent= onEvent)
}


@Composable
fun ListExpensesScreen(
    state: ExpenseListState,
    onAddClick: () -> Unit,
    onExpenseClick: (Expense) -> Unit,
    onEvent: (ExpenseListEvent) -> Unit
) {
    val sortTypes = sortTypeToStringList()
    var selectedSortTypeIndex by remember {
        mutableStateOf(0)
    }

    Column (modifier = Modifier
        .fillMaxSize()
        .background(DarkBlue)
        .statusBarsPadding(), horizontalAlignment = Alignment.CenterHorizontally){
        // add drop down for ordering list
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = DesertWhite
        ){
            Column {
                Text(text = stringResource(id = R.string.welcome),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(20.dp))
                Row (verticalAlignment = Alignment.CenterVertically,
                    modifier =  Modifier.fillMaxWidth()){
                    IconButton(
                        onClick = { onAddClick() },
                        modifier = Modifier
                            .background(SandYellow)
                            .border(
                                width = 1.dp,
                                SandYellow,
                                shape = RoundedCornerShape(
                                    topStart = 32.dp,
                                    topEnd = 32.dp,
                                    bottomEnd = 32.dp,
                                    bottomStart = 32.dp
                                )
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Add expense")
                    }
                    Spacer(modifier = Modifier.width(5.dp))
                    Column(modifier = Modifier.requiredHeight(70.dp)) {
                        Text(text = stringResource(id = R.string.order_by))
                        Spacer(modifier = Modifier.height(10.dp))
                        AppDropdown(itemsList = sortTypes,
                            selectedPosition = selectedSortTypeIndex,
                            onItemClick = { index ->
                                selectedSortTypeIndex = index
                                onEvent(ExpenseListEvent.SortExpenses(getSortType(index)))
                                },
                            modifier = Modifier.requiredHeight(50.dp))
                    }

                    Spacer(modifier = Modifier.width(5.dp))
                }
                Spacer(modifier = Modifier.height(20.dp))
                if (state.expenses.isEmpty()) {
                    Text(text = stringResource(id = R.string.no_expenses),
                        style = MaterialTheme.typography.titleLarge)
                } else {
                    ExpenseList(expenses = state.expenses,
                        onExpenseClick = { onExpenseClick(it)},
                        onDeleteClick = onEvent)
                }

            }

        }

    }

}

@Composable
private fun sortTypeToStringList() : List<String>{
    val resultList = arrayListOf<String>()
    val types = SortType.values()
    for (i in types.indices) {
        val typeName = when (types[i]) {
            SortType.DATE -> stringResource(R.string.date)
            SortType.TOTAL -> stringResource(R.string.total)
            SortType.NAME -> stringResource(R.string.name)
        }
        resultList.add(typeName)
    }
    return resultList
}

private fun getSortType(index: Int) : SortType{
    return when (index) {
        0 -> SortType.DATE
        1 -> SortType.NAME
        2 -> SortType.TOTAL
        else -> SortType.DATE
    }

}