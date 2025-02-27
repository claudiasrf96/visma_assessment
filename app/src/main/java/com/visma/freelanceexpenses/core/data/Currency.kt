package com.visma.freelanceexpenses.core.data

data class Currency(val currencyCode: String, val currencySymbol: String)

fun currencyList() : List<Currency> {
    return listOf(
        Currency("EUR", "€"),
        Currency("USD", "$"),
        Currency("GBP", "£")
    )
}

fun currencyIndex(currencyCode: String) : Int {
    return when(currencyCode) {
        "EUR" -> 0
        "USD" -> 1
        "GBP" -> 2
        else -> 0
    }
}