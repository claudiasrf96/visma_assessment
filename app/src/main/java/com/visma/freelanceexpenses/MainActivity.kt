package com.visma.freelanceexpenses

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.visma.freelanceexpenses.core.app.Routes
import com.visma.freelanceexpenses.ui.theme.FreelanceExpensesTheme
import com.visma.freelanceexpenses.view.expense_list.ListExpensesScreenRoot
import com.visma.freelanceexpenses.view.expense_upsert.UpsertExpenseScreen
import com.visma.freelanceexpenses.viewmodel.ExpenseListViewModel
import com.visma.freelanceexpenses.viewmodel.ExpenseUpsertViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FreelanceExpensesTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.expenseListRoute) {
                    composable(route = Routes.expenseListRoute){
                        val viewModel = hiltViewModel<ExpenseListViewModel>()

                        ListExpensesScreenRoot(
                            viewModel = viewModel,
                            onExpenseAddClick = {
                                navController.navigate(Routes.expenseUpsertRoute)
                            },
                            onExpenseClick = { expense ->
                                // with expense object
                                navController.navigate(Routes.expenseUpsertRoute +
                                        "?expenseId=${expense.id}")
                            },
                            onEvent = viewModel::onEvent
                        )
                    }

                    composable(
                        route = Routes.expenseUpsertRoute + "?expenseId={expenseId}",
                        arguments = listOf(navArgument(
                            name = "expenseId"
                        ) {
                            type = NavType.IntType
                            defaultValue = -1
                        })
                    ) {
                        val viewModel = hiltViewModel<ExpenseUpsertViewModel>()
                        UpsertExpenseScreen(
                            navController = navController,
                            viewModel= viewModel)
                    }
                }
            }
        }
    }
}

