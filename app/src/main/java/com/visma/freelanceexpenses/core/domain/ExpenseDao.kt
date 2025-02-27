package com.visma.freelanceexpenses.core.domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.visma.freelanceexpenses.core.domain.model.Expense
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Upsert
    suspend fun upsertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT * FROM Expense")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM Expense WHERE id = :id")
    suspend fun getExpensesById(id: Int): Expense?
}