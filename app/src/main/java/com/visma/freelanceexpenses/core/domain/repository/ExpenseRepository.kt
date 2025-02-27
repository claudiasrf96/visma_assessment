package com.visma.freelanceexpenses.core.domain.repository

import com.visma.freelanceexpenses.core.domain.model.Expense
import com.visma.freelanceexpenses.view.expense_list.SortType
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun upsertExpense(expense: Expense)

    suspend fun deleteExpense(expense: Expense)

    suspend fun getExpensesById(id: Int) : Expense?

    fun getExpenses(): Flow<List<Expense>>

    suspend fun deleteWithId(id: Int)

}