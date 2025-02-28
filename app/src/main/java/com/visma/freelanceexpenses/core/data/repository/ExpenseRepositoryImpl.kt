package com.visma.freelanceexpenses.core.data.repository

import androidx.paging.PagingSource
import com.visma.freelanceexpenses.core.domain.ExpenseDao
import com.visma.freelanceexpenses.core.domain.model.Expense
import com.visma.freelanceexpenses.core.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class ExpenseRepositoryImpl(private val dao: ExpenseDao) : ExpenseRepository {
    override suspend fun upsertExpense(expense: Expense) {
        dao.upsertExpense(expense)
    }

    override suspend fun deleteExpense(expense: Expense) {
        dao.deleteExpense(expense)
    }

    override suspend fun getExpensesById(id: Int): Expense? {
        return dao.getExpensesById(id)
    }

    override fun getExpenses(): Flow<List<Expense>>{
        return dao.getAllExpenses()
    }

    override fun getExpensesOrderByDatePaged(): PagingSource<Int, Expense> {
        return dao.getExpensesOrderedByDatePaged()
    }

    override fun getExpensesOrderByNamePaged(): PagingSource<Int, Expense> {
        return dao.getExpensesOrderedByNamePaged()
    }

    override fun getExpensesOrderByTotalPaged(): PagingSource<Int, Expense> {
        return dao.getExpensesOrderedByTotalPaged()
    }


}