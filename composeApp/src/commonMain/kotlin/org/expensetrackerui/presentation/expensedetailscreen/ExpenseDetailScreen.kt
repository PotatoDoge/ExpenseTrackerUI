package org.expensetrackerui.presentation.expensedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow // Import FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category // Keep Category icon for the detail row
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description // Use Description icon for name in card
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.UsbOff
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.expensetrackerui.data.model.Expense
import org.expensetrackerui.data.model.ExpenseCategory
import org.expensetrackerui.data.model.PaymentMethod
import org.expensetrackerui.data.model.TagWithColor // Import TagWithColor
import org.expensetrackerui.util.CurrencyFormatter
import org.expensetrackerui.util.formatDateForDisplay

@OptIn(ExperimentalLayoutApi::class) // Add this opt-in for FlowRow
@Composable
fun ExpenseDetailScreen(
    expense: Expense,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Top Bar with Back Button and Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Detalle del Gasto",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Hero Section: Amount, Expense Name, Date
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // **Expense Name and Icon (Moved into the card)**
                    if (expense.name.isNotBlank()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description, // Icon for name
                                contentDescription = "Nombre del Gasto",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = expense.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        // If no name, ensure there's still some spacing or a placeholder if needed
                        Spacer(modifier = Modifier.height(8.dp))
                    }


                    // Amount
                    Text(
                        text = CurrencyFormatter.formatAmount(expense.amount),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Date (remains in card)
                    Text(
                        text = formatDateForDisplay(expense.date),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Details Section (below the hero)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // **Category section (Moved outside the card)**
                ExpenseDetailRow(
                    icon = expense.category.icon(),
                    title = "Categoría",
                    value = expense.category.name.replace("_", " ").lowercase()
                        .replaceFirstChar { it.uppercase() },
                    iconTint = MaterialTheme.colorScheme.secondary // Or a color that fits categories
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Payment Method section (remains outside)
                ExpenseDetailRow(
                    icon = expense.paymentMethod.icon(),
                    title = "Método de Pago",
                    value = expense.paymentMethod.name.replace("_", " ").lowercase()
                        .replaceFirstChar { it.uppercase() },
                    iconTint = MaterialTheme.colorScheme.tertiary
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Tags section (remains outside)
                if (expense.tags.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        ExpenseDetailRowHeader(
                            icon = Icons.Default.Style,
                            title = "Etiquetas",
                            iconTint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            expense.tags.forEach { tag ->
                                DisplayTagChip(tagWithColor = tag)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

// ... (ExpenseDetailRow, ExpenseDetailRowHeader, DisplayTagChip, ExpenseCategory.icon(), PaymentMethod.icon() remain unchanged from previous versions)

@Composable
private fun ExpenseDetailRow(
    icon: ImageVector,
    title: String,
    value: String,
    iconTint: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
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

@Composable
private fun ExpenseDetailRowHeader(
    icon: ImageVector,
    title: String,
    iconTint: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconTint.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DisplayTagChip(
    tagWithColor: TagWithColor
) {
    Surface(
        modifier = Modifier,
        shape = RoundedCornerShape(8.dp),
        color = tagWithColor.color,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = tagWithColor.name,
                color = if (tagWithColor.color.luminance() > 0.5f) Color.Black else Color.White,
                fontSize = 14.sp
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
        else -> Icons.Default.Category
    }
}

fun PaymentMethod.icon(): ImageVector {
    return when (this) {
        PaymentMethod.BANAMEX_ORO,
        PaymentMethod.BANAMEX_AZUL,
        PaymentMethod.NU_CREDITO -> Icons.Default.CreditCard

        PaymentMethod.HEY_BANCO,
        PaymentMethod.NU_DEBITO,
        PaymentMethod.BANAMEX_DEBITO -> Icons.Default.AccountBalanceWallet

        PaymentMethod.VALES_DE_DESPENSA -> Icons.Default.Loyalty
        PaymentMethod.EFECTIVO -> Icons.Default.AttachMoney
        PaymentMethod.OTRO -> Icons.Default.HelpOutline
    }
}