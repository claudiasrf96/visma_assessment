package com.visma.freelanceexpenses.core.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Expense(
    val name: String,
    val imageLocation: String? = null,
    val currency: String,
    val total: Double,
    val description: String? = null,
    val date: String,
    val category: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)