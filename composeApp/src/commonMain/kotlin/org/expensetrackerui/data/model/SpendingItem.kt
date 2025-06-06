package org.expensetrackerui.data.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class SpendingItem(
    val name: String,
    val amount: Double,
    val totalMonthSpending: Double,
    val color: Color = Color.Gray
)