package org.expensetrackerui.data.repository.impl
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Movie
import org.expensetrackerui.data.repository.TransactionRepository
import org.expensetrackerui.data.model.Transaction
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsMotorsports

class TransactionRepositoryImpl : TransactionRepository {
    override fun getRecentTransactions(): List<Transaction> {
        return listOf(
            Transaction(Icons.Default.ShoppingCart, "Supermercado XYZ", "Abarrotes", 50.00),
            Transaction(Icons.Default.LocalMall, "Tienda de Ropa Z", "Vestimenta", -120.00),
            Transaction(Icons.Default.SportsMotorsports, "Gasolinera Pemex", "Transporte", -35.00),
            Transaction(Icons.Default.Movie, "Cinepolis", "Entretenimiento", -25.00),
            Transaction(Icons.Default.Fastfood, "Restaurante ABC", "Comida Fuera", -40.00)
        )
    }
}