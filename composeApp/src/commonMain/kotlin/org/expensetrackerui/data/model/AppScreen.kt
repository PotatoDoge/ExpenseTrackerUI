package org.expensetrackerui.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppScreen(val route: String, val icon: ImageVector, val label: String) {
    object Home : AppScreen("home", Icons.Default.Home, "Inicio")
    object AddExpense : AppScreen("add_expense", Icons.Default.Add, "Añadir")
    object ExpensesList : AppScreen("expenses_list", Icons.Default.Menu, "Gastos")
    object Categories : AppScreen("categories", Icons.Default.Category, "Categorías")
    object Settings : AppScreen("settings", Icons.Default.Settings, "Ajustes")
}