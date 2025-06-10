package org.expensetrackerui.presentation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
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
                onClick = { onScreenSelected(screen) }
            )
        }
    }
}