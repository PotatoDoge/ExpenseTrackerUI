package org.expensetrackerui.data.model

import org.jetbrains.compose.resources.DrawableResource

data class FinancialTip(
    val title: String,
    val subtitle: String,
    val imageRes: DrawableResource? = null
)