package com.visma.freelanceexpenses.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.visma.freelanceexpenses.core.domain.model.Expense
import com.visma.freelanceexpenses.core.domain.ExpenseDao

@Database(
    entities = [Expense::class],
    version = 1,
    exportSchema = false
)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract val expenseDao: ExpenseDao

    companion object {
        const val DBNAME = "expense.db"
    }
}
