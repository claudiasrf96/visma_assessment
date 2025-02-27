package com.visma.freelanceexpenses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.visma.freelanceexpenses.core.app.Routes
import com.visma.freelanceexpenses.ui.theme.FreelanceExpensesTheme
import com.visma.freelanceexpenses.view.expense_list.ListExpensesScreenRoot
import com.visma.freelanceexpenses.view.expense_upsert.UpsertExpenseScreen
import com.visma.freelanceexpenses.viewmodel.ExpenseListViewModel
import com.visma.freelanceexpenses.viewmodel.ExpenseUpsertViewModel
import dagger.hilt.android.AndroidEntryPoint

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FreelanceExpensesTheme {
            }
        }
    }
}

