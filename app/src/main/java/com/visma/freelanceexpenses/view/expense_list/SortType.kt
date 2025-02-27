package com.visma.freelanceexpenses.view.expense_list

import android.content.res.Resources
import com.visma.freelanceexpenses.R

enum class SortType {
    DATE,
    NAME,
    TOTAL;

    override fun toString(): String {
        return when(this) {
            DATE -> Resources.getSystem().getString(R.string.date)
            NAME -> Resources.getSystem().getString(R.string.name)
            TOTAL -> Resources.getSystem().getString(R.string.total)
        }
    }
}