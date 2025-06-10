package org.expensetrackerui.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.datetime.LocalDate

data class Expense(
    val id: String, // Or Int, depending on your ID generation
    val name: String,
    val amount: Double,
    val currency: Currency,
    val date: LocalDate,
    val paymentMethod: PaymentMethod,
    val category: ExpenseCategory,
    val tags: List<TagWithColor> = emptyList()
) {
    fun toTransactionForDisplay(): Transaction {
        val transactionIcon: ImageVector = when (this.category) {
            ExpenseCategory.NECESIDAD -> Icons.Default.ShoppingCart // Groceries, essential items
            ExpenseCategory.GASTO_NO_PLANEADO -> Icons.Default.LocalHospital // Unexpected medical, repairs
            ExpenseCategory.GUSTO -> Icons.Default.Fastfood // Dining out, entertainment
            ExpenseCategory.DEUDA -> Icons.Default.Paid // Debt payments
        }

        return Transaction(
            icon = transactionIcon,
            storeName = this.name,
            category = this.category.name,
            amount = this.amount
        )
    }
}

enum class Currency {
    USD, MXN, CAD // Example types
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
