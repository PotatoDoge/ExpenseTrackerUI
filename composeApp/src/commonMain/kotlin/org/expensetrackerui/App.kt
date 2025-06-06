package org.expensetrackerui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.expensetrackerui.data.model.AppScreen
import org.expensetrackerui.presentation.MainViewModel
import org.expensetrackerui.presentation.addexpense.AddExpenseScreen
import org.expensetrackerui.presentation.home.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        // Create an instance of your MainViewModel
        val mainViewModel: MainViewModel = remember { MainViewModel() } // Or use DI
        val currentScreen = mainViewModel.currentScreen // Corrected: removed 'by'

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                // Our custom Bottom Navigation Bar
                AppBottomNavigationBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { screen -> mainViewModel.selectScreen(screen) }
                )
            }
        ) { paddingValues ->
            // The content of the Scaffold is passed paddingValues by the Scaffold itself.
            // We need to apply these paddingValues to the root of our displayed screen content.
            when (currentScreen) {
                // Pass paddingValues as a modifier to each screen
                AppScreen.Home -> HomeScreen(modifier = Modifier.padding(paddingValues))
                AppScreen.AddExpense -> AddExpenseScreen(
                    modifier = Modifier.padding(paddingValues), // Passed here
                    onClose = {
                        // For now, closing the AddExpenseScreen will navigate back to Home
                        mainViewModel.selectScreen(AppScreen.Home)
                    }
                )
                AppScreen.ExpensesList -> PlaceholderScreen("Gastos", modifier = Modifier.padding(paddingValues))
                AppScreen.Notifications -> PlaceholderScreen("Alertas", modifier = Modifier.padding(paddingValues))
                AppScreen.Settings -> PlaceholderScreen("Ajustes", modifier = Modifier.padding(paddingValues))
            }
        }
    }
}

// Helper Composable for the actual Bottom Navigation Bar
@Composable
fun AppBottomNavigationBar(
    currentScreen: AppScreen,
    onScreenSelected: (AppScreen) -> Unit
) {
    val items = listOf(
        AppScreen.Home,
        AppScreen.AddExpense,
        AppScreen.ExpensesList,
        AppScreen.Notifications,
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

// A simple placeholder for other screens
@Composable
fun PlaceholderScreen(
    name: String,
    modifier: Modifier = Modifier // Added modifier parameter
) {
    Column(
        modifier = modifier // Applied here
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Contenido de $name", fontSize = 20.sp)
    }
}