package org.expensetrackerui.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.expensetrackerui.data.model.Expense
import org.expensetrackerui.data.model.ExpenseCategory
import org.expensetrackerui.util.CurrencyFormatter

@Composable
fun ExpenseListItem(expense: Expense) {
    val transactionIcon: ImageVector = when (expense.category) {
        ExpenseCategory.NECESIDAD -> Icons.Default.ShoppingCart
        ExpenseCategory.GASTO_NO_PLANEADO -> Icons.Default.LocalHospital
        ExpenseCategory.GUSTO -> Icons.Default.Fastfood
        ExpenseCategory.DEUDA -> Icons.Default.Paid
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = transactionIcon,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = expense.name,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = expense.category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() }, // Use expense.category.name
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        Text(
            text = CurrencyFormatter.formatAmount(expense.amount), // Use expense.amount directly
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = if (expense.amount < 0) Color(0XFFbf100a) else Color(0XFF2d8c3a),
            textAlign = TextAlign.End
        )
    }
}