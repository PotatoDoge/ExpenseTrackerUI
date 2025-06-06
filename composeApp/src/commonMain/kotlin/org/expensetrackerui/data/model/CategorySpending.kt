package org.expensetrackerui.data.model

import androidx.compose.ui.graphics.vector.ImageVector

data class CategorySpending(
    val name: String,
    val amount: Double,
    val icon: ImageVector, // For contextual icon
    val totalMonthSpending: Double // Needed for proportional bar
)