package com.visma.freelanceexpenses

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.visma.freelanceexpenses.core.app.ArgumentNames
import com.visma.freelanceexpenses.core.app.Routes
import com.visma.freelanceexpenses.ui.theme.FreelanceExpensesTheme
import com.visma.freelanceexpenses.view.camera.CameraPreview
import com.visma.freelanceexpenses.view.expense_list.ListExpensesScreenRoot
import com.visma.freelanceexpenses.view.expense_upsert.UpsertExpenseScreen
import com.visma.freelanceexpenses.viewmodel.ExpenseListViewModel
import com.visma.freelanceexpenses.viewmodel.ExpenseUpsertViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val hasPermissions = permissions.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (!hasPermissions){
            ActivityCompat.requestPermissions(
                this, permissions, 0
            )
        }

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
                        route = Routes.expenseUpsertRoute +
                                "?${ArgumentNames.expenseId}={${ArgumentNames.expenseId}}" +
                                "&${ArgumentNames.photoUri}={${ArgumentNames.photoUri}}",
                        arguments = listOf(navArgument(
                            name = ArgumentNames.expenseId
                        ) {
                            type = NavType.IntType
                            defaultValue = -1
                        }, navArgument(
                            name = ArgumentNames.photoUri
                        ) {
                            type = NavType.StringType
                            defaultValue = ""
                        })
                    ) {
                        val viewModel = hiltViewModel<ExpenseUpsertViewModel>()
                        UpsertExpenseScreen(
                            navController = navController,
                            viewModel= viewModel, onClickAddPhoto = {
                                navController.navigate(Routes.camera_screen)
                            })
                    }

                    composable(route = Routes.camera_screen){
                        CameraPreview(navController)
                    }
                }
            }
        }
    }
}

