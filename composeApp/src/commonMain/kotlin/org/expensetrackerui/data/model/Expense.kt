package org.expensetrackerui.data.model

import kotlinx.datetime.LocalDate

data class Expense(
    val id: String, // Or Int, depending on your ID generation
    val name: String,
    val amount: Double,
    val currency: Currency,
    val date: LocalDate,
    val location: String?,
    val paymentMethod: PaymentMethod,
    val category: ExpenseCategory,
    val tags: List<ExpenseTag> = emptyList()
)

enum class Currency {
    USD, MXN, CAD // Example types
}

enum class PaymentMethod {
    CREDIT_CARD, DEBIT_CARD, CASH, TRANSFER, OTHER // Example payment methods
}

enum class ExpenseCategory {
    FOOD, TRANSPORTATION, ENTERTAINMENT, SHOPPING, OTHER // Your defined categories
}

enum class ExpenseTag {
    URGENT, OPTIONAL, NECESSARY // Your defined tags
}
