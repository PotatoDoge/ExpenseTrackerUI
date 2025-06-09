package org.expensetrackerui.data.repository.impl

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.OtherHouses
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsMotorsports
import androidx.compose.ui.graphics.Color
import org.expensetrackerui.data.model.CategorySpending
import org.expensetrackerui.data.model.PaymentMethodSpending
import org.expensetrackerui.data.model.SpendingItem
import org.expensetrackerui.data.repository.SpendingRepository

class SpendingRepositoryImpl : SpendingRepository {

    private val paymentMethodColors = listOf(
        Color(0xFF57B8FF),
        Color(0xFF81B29A),
        Color(0xFF7B506F),
        Color(0xFFE07A5F),
        Color(0xFF8B008B),
        Color(0xFFECD444)
    )

    private val categoryColors = listOf(
        Color(0xFFFB4B4E),
        Color(0xFFFFAD05),
        Color(0xFFF4BFDB),
        Color(0xFF7B506F),
        Color(0xFFA5E6BA),
        Color(0xFF9AC6C5)
    )

    override fun getPaymentMethodSpending(): List<PaymentMethodSpending> {
        return listOf(
            PaymentMethodSpending("Banamex Oro", 500.00, 1534.56),
            PaymentMethodSpending("BBVA Azul", 400.00, 1534.56),
            PaymentMethodSpending("Banamex Débito (priority)", 334.56, 1534.56),
            PaymentMethodSpending("Efectivo", 300.00, 1534.56),
            PaymentMethodSpending("Otro", 0.0, 1534.56)
        )
    }

    override fun getCategorySpending(): List<CategorySpending> {
        return listOf(
            CategorySpending("Comida", 450.00, Icons.Default.Fastfood, 1534.56),
            CategorySpending("Compras", 300.00, Icons.Default.ShoppingCart, 1534.56),
            CategorySpending("Transporte", 200.00, Icons.Default.SportsMotorsports, 1534.56),
            CategorySpending("Entretenimiento", 150.00, Icons.Default.Movie, 1534.56),
            CategorySpending("Otros", 134.56, Icons.Default.OtherHouses, 1534.56)
        )
    }

    override fun getMappedPaymentMethodSpending(): List<SpendingItem> {
        return getPaymentMethodSpending().mapIndexed { index, method ->
            SpendingItem(
                name = method.name,
                amount = method.amount,
                totalMonthSpending = method.totalMonthSpending,
                color = paymentMethodColors.getOrElse(index) { Color.Gray }
            )
        }
    }

    override fun getMappedCategorySpending(): List<SpendingItem> {
        return getCategorySpending().mapIndexed { index, category ->
            SpendingItem(
                name = category.name,
                amount = category.amount,
                totalMonthSpending = category.totalMonthSpending,
                color = categoryColors.getOrElse(index) { Color.Gray }
            )
        }
    }
}