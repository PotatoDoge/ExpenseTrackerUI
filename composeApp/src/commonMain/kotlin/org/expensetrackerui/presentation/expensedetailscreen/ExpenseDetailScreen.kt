package org.expensetrackerui.presentation.expensedetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.UsbOff
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.expensetrackerui.data.model.Expense
import org.expensetrackerui.data.model.ExpenseCategory
import org.expensetrackerui.util.CurrencyFormatter
import org.expensetrackerui.util.formatDateForDisplay

@Composable
fun ExpenseDetailScreen(
    expense: Expense,
    onClose: () -> Unit,
    modifier: Modifier = Modifier // This is already correctly defined
) {

    Surface(
        modifier = modifier.fillMaxSize(), // Apply the inherited modifier here
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Detalle del Gasto",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Category section
            ExpenseDetailRow(
                title = "Categoría",
                value = expense.category.name.replace("_", " ").lowercase()
                    .replaceFirstChar { it.uppercase() }
            )

            // Description section
            if (expense.name.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                ExpenseDetailRow(
                    title = "Nombre",
                    value = expense.name
                )
            }

            // Amount section
            Spacer(modifier = Modifier.height(8.dp))
            ExpenseDetailRow(
                title = "Monto",
                value = CurrencyFormatter.formatAmount(expense.amount)
            )

            // Date section
            Spacer(modifier = Modifier.height(8.dp))
            ExpenseDetailRow(
                title = "Fecha",
                value = formatDateForDisplay(expense.date)
            )

            // Payment Method section
            Spacer(modifier = Modifier.height(8.dp))
            ExpenseDetailRow(
                title = "Método de Pago",
                value = expense.paymentMethod.name.replace("_", " ").lowercase()
                    .replaceFirstChar { it.uppercase() }
            )

            // Tags section
            if (expense.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                ExpenseDetailRow(
                    title = "Etiquetas",
                    value = expense.tags.joinToString(", ")
                )
            }
        }
    }
}

@Composable
private fun ExpenseDetailRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

fun ExpenseCategory.icon(): ImageVector {
    return when (this) {
        ExpenseCategory.NECESIDAD -> Icons.Default.Water
        ExpenseCategory.GASTO_NO_PLANEADO -> Icons.Default.MoneyOff
        ExpenseCategory.GUSTO -> Icons.Default.UsbOff
        ExpenseCategory.DEUDA -> Icons.Default.PinDrop
    }
}