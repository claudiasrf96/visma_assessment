package com.visma.freelanceexpenses.view.expense_upsert

sealed interface ExpenseUpsertEvent {
    object SaveExpense: ExpenseUpsertEvent
    data class SetName(val name: String) : ExpenseUpsertEvent
    data class SetImageLocation(val imageLocation: String?) : ExpenseUpsertEvent
    data class SetCurrency(val currency: String) : ExpenseUpsertEvent
    data class SetTotal(val total: Double) : ExpenseUpsertEvent
    data class SetDescription(val description: String) : ExpenseUpsertEvent
    data class SetDate(val date: String) : ExpenseUpsertEvent
    data class SetCategory(val category: Int) : ExpenseUpsertEvent
}