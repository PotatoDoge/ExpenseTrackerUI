package org.expensetrackerui.data.model

import androidx.compose.ui.graphics.vector.ImageVector

data class Transaction(
    val icon: ImageVector,
    val storeName: String,
    val category: String,
    val amount: Double
)