package com.visma.freelanceexpenses.core.domain.repository

import androidx.paging.PagingSource
import com.visma.freelanceexpenses.core.domain.model.Expense
import com.visma.freelanceexpenses.view.expense_list.SortType
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    suspend fun upsertExpense(expense: Expense)

    suspend fun deleteExpense(expense: Expense)

    suspend fun getExpensesById(id: Int) : Expense?

    fun getExpenses(): Flow<List<Expense>>

    fun getExpensesOrderByDatePaged(): PagingSource<Int, Expense>

    fun getExpensesOrderByNamePaged(): PagingSource<Int, Expense>
    fun getExpensesOrderByTotalPaged(): PagingSource<Int, Expense>
}