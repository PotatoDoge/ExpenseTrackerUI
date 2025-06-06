package org.expensetrackerui.util

expect object CurrencyFormatter {
    fun formatAmount(amount: Double): String
}