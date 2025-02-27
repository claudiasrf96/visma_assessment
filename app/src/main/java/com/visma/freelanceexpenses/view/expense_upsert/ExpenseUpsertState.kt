package com.visma.freelanceexpenses.view.expense_upsert

import com.visma.freelanceexpenses.core.domain.model.Expense
import com.visma.freelanceexpenses.core.data.ExpenseCategory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ExpenseUpsertState(val name: String = "",
                              val imageLocation: String? = null,
                              val currency: String = "",
                              val total: Double = 0.0,
                              val description: String? = null,
                              val date: String = LocalDate.now().format(DateTimeFormatter.ISO_DATE),
                              val category: Int = ExpenseCategory.NO_CATEGORY.ordinal,
                              val expense: Expense? = null)
