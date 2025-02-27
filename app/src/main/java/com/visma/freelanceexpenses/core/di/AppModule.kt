package com.visma.freelanceexpenses.core.di

import android.content.Context
import androidx.room.Room
import com.visma.freelanceexpenses.core.ExpenseDatabase
import com.visma.freelanceexpenses.core.data.repository.ExpenseRepositoryImpl
import com.visma.freelanceexpenses.core.domain.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : ExpenseDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            ExpenseDatabase.DBNAME)
            .build()
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(db: ExpenseDatabase) : ExpenseRepository {
        return ExpenseRepositoryImpl(db.expenseDao)
    }
}