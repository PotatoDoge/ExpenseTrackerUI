package org.expensetrackerui.data.model

data class PaymentMethodSpending(
    val name: String,
    val amount: Double,
    val totalMonthSpending: Double // Needed for proportional bar
)