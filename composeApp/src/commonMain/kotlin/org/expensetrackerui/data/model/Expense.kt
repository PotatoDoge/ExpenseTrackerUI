package org.expensetrackerui.data.model

import kotlinx.datetime.LocalDate

data class Expense(
    val id: String,
    val name: String,
    val amount: Double,
    val currency: Currency,
    val date: LocalDate,
    val paymentMethod: PaymentMethod,
    val category: ExpenseCategory,
    val tags: List<TagWithColor> = emptyList()
)

enum class Currency {
    USD, MXN, CAD
}

enum class PaymentMethod {
    BANAMEX_ORO, BANAMEX_AZUL, VALES_DE_DESPENSA, HEY_BANCO, NU_CREDITO,
    NU_DEBITO, EFECTIVO, BANAMEX_DEBITO, OTRO
}

enum class ExpenseCategory {
    NECESIDAD, GASTO_NO_PLANEADO, GUSTO, DEUDA
}

enum class ExpenseTag {
    URGENT, OPTIONAL, NECESSARY // Your defined tags
}
