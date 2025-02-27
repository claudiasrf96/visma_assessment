package com.visma.freelanceexpenses.core.data

data class Currency(val currencyCode: String, val currencySymbol: String)

fun currencyList() : List<Currency> {
    return listOf(
        Currency("EUR", "€"),
        Currency("USD", "$"),
        Currency("GBP", "£")
    )
}