package org.expensetrackerui.presentation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.expensetrackerui.data.model.AppScreen

@Composable
fun AppBottomNavigationBar(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {
    val items = listOf(
        AppScreen.Home,
        AppScreen.AddExpense,
        AppScreen.ExpensesList,
        AppScreen.Categories,
        AppScreen.Settings
    )

    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.label) },
                selected = currentScreen == screen,
                onClick = { onScreenSelected(screen) },
                colors = NavigationBarItemDefaults.colors(
                    // Set indicatorColor to Transparent to remove background change
                    indicatorColor = Color.Transparent,
                    // Define icon colors based on selection state
                    selectedIconColor = MaterialTheme.colorScheme.primary, // Color when selected
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), // Color when not selected
                    // If you have labels, you might want to set their colors too
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            )
        }
    }
}