package com.visma.freelanceexpenses.view.expense_upsert

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.visma.freelanceexpenses.R
import com.visma.freelanceexpenses.core.app.ArgumentNames
import com.visma.freelanceexpenses.core.data.ExpenseCategory
import com.visma.freelanceexpenses.core.data.currencyIndex
import com.visma.freelanceexpenses.core.data.currencyList
import com.visma.freelanceexpenses.ui.theme.Green
import com.visma.freelanceexpenses.ui.theme.SandYellow
import com.visma.freelanceexpenses.view.components.AppDropdown
import com.visma.freelanceexpenses.view.components.DatePickerModal
import com.visma.freelanceexpenses.viewmodel.ExpenseUpsertViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun UpsertExpenseScreen(
    navController: NavController,
    viewModel: ExpenseUpsertViewModel = hiltViewModel(),
    onClickAddPhoto: () -> Unit
)
{
    val scrollState = rememberScrollState()

    val categories = expenseCategoriesToStringList()
    val currencies = currencyList().map { c -> c.currencyCode }
    var showModal by remember { mutableStateOf(false) }
    val selectedDate by remember { mutableStateOf<Long?>(null) }

    val backStackEntry = navController.currentBackStackEntryAsState().value
    val savedStateHandle = backStackEntry?.savedStateHandle
    val uri = savedStateHandle?.get<String>(ArgumentNames.photoUri)
    if (uri != null) {
        val photoUri = if(uri.isBlank() ?: true) null else uri
        viewModel.onEvent(ExpenseUpsertEvent.SetImageLocation(photoUri))
    }

    Surface{
        if (showModal) {
            DatePickerModal(
                onDateSelected = {
                    if (it != null){
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val date = formatter.format(Date(it))
                        viewModel.onEvent(ExpenseUpsertEvent.SetDate(date))
                    }
                },
                onDismiss = { showModal = false }
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(all = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(scrollState)
        ) {
            Row {
                Button(onClick = { navController.navigateUp() },
                    modifier= Modifier.size(30.dp),
                    shape = CircleShape,
                    border= BorderStroke(1.dp, SandYellow),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SandYellow)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(id = R.string.go_back),
                        modifier = Modifier.size(15.dp))
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = stringResource(id = R.string.expense),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface)
            }

            OutlinedTextField(
                value = viewModel.name.value,
                onValueChange = {
                    viewModel.onEvent(ExpenseUpsertEvent.SetName(it))
                },
                label = {
                    Text(text = stringResource(id = R.string.name))
                }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = R.string.currency))
            AppDropdown(itemsList = currencies,
                selectedPosition = currencyIndex(viewModel.currency.value),
                onItemClick = {
                    index -> viewModel.onEvent(ExpenseUpsertEvent.SetCurrency(currencies[index]))
            }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                value = viewModel.total.value.toString(),
                onValueChange = {
                    val totalVal = it.toDouble()
                    viewModel.onEvent(ExpenseUpsertEvent.SetTotal(totalVal))
                },
                label = {
                    Text(text = stringResource(id = R.string.total))
                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = viewModel.date.value,
                onValueChange = { },
                readOnly = true,
                label = {
                    Text(text = stringResource(id = R.string.date))
                }, trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = stringResource(id = R.string.select_date))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(selectedDate) {
                        awaitEachGesture {
                            awaitFirstDown(pass = PointerEventPass.Initial)
                            val upEvent = waitForUpOrCancellation(pass = PointerEventPass.Initial)
                            if (upEvent != null) {
                                showModal = true
                            }
                        }
                    }
            )

            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = R.string.category))
            AppDropdown(itemsList = categories, selectedPosition = viewModel.category.value,
                onItemClick = {
                    index -> viewModel.onEvent(ExpenseUpsertEvent.SetCategory(index))

            }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = viewModel.description.value ?: "",
                onValueChange = {
                    viewModel.onEvent(ExpenseUpsertEvent.SetCurrency(it))
                },
                label = {
                    Text(text = stringResource(id = R.string.description))
                }, modifier = Modifier.fillMaxWidth()
            )

            if (viewModel.imageLocation.value != null) {
                val file = File(viewModel.imageLocation.value!!)
                val painter = rememberAsyncImagePainter(file)

                Image(
                    painter = painter,
                    contentDescription = stringResource(id = R.string.invoice_image),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Button(modifier = Modifier.fillMaxWidth(),
                onClick = { onClickAddPhoto() }) {
                Text(
                    text = stringResource(id = R.string.add_expense_invoice_photo),
                    color = Color.White)
            }


            Spacer(modifier = Modifier.height(30.dp))
            Button(modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green, // White background
                    contentColor = Color.Black  // Black text color
                ),
                onClick = {
                viewModel.onEvent(ExpenseUpsertEvent.SaveExpense)
                navController.navigateUp()
            }) {
                Text(
                    text = stringResource(id = R.string.save_expense),
                    color = Color.White)
            }
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
