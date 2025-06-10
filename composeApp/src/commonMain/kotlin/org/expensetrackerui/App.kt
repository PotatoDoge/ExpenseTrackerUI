package org.expensetrackerui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember // Keep for Preview defaults
import androidx.compose.ui.Modifier
import org.expensetrackerui.data.model.AppScreen
import org.expensetrackerui.presentation.AppBottomNavigationBar
import org.expensetrackerui.presentation.MainViewModel
import org.expensetrackerui.presentation.PlaceholderScreen
import org.expensetrackerui.presentation.addexpense.AddExpenseScreen
import org.expensetrackerui.presentation.addexpense.AddExpenseViewModel
import org.expensetrackerui.presentation.home.HomeScreen
import org.expensetrackerui.presentation.home.HomeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.expensetrackerui.util.preview_dummies.DummyBudgetRepository
import org.expensetrackerui.util.preview_dummies.DummyFinancialTipsRepository
import org.expensetrackerui.util.preview_dummies.DummyGetExpenseCategoriesUseCase
import org.expensetrackerui.util.preview_dummies.DummyGetExpenseTagsUseCase
import org.expensetrackerui.util.preview_dummies.DummyGetPaymentMethodsUseCase
import org.expensetrackerui.util.preview_dummies.DummySaveExpenseUseCase
import org.expensetrackerui.util.preview_dummies.DummySpendingRepository
import org.expensetrackerui.util.preview_dummies.DummyTransactionRepository


@Composable
@Preview
fun App(
    mainViewModel: MainViewModel = remember { MainViewModel() },
    homeViewModel: HomeViewModel = remember {
        HomeViewModel(
            budgetRepository = DummyBudgetRepository(),
            spendingRepository = DummySpendingRepository(),
            transactionRepository = DummyTransactionRepository(),
            financialTipsRepository = DummyFinancialTipsRepository()
        )
    },
    addExpenseViewModel: AddExpenseViewModel = remember {
        AddExpenseViewModel(
            saveExpenseRepository = DummySaveExpenseUseCase(),
            getExpenseCategoriesRepository = DummyGetExpenseCategoriesUseCase(),
            getExpenseTagsRepository = DummyGetExpenseTagsUseCase(),
            getPaymentMethodsRepository = DummyGetPaymentMethodsUseCase()
        )
    }
) {
    MaterialTheme {
        val currentScreen = mainViewModel.currentScreen.collectAsState().value

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                AppBottomNavigationBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { screen -> mainViewModel.selectScreen(screen) }
                )
            }
        ) { paddingValues ->
            when (currentScreen) {
                AppScreen.Home -> HomeScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = homeViewModel
                )

                AppScreen.AddExpense -> AddExpenseScreen(
                    modifier = Modifier.padding(paddingValues),
                    onClose = {
                        mainViewModel.selectScreen(AppScreen.Home)
                    },
                    viewModel = addExpenseViewModel
                )

                AppScreen.ExpensesList -> PlaceholderScreen(
                    "Gastos",
                    modifier = Modifier.padding(paddingValues)
                )

                AppScreen.Categories -> PlaceholderScreen(
                    "Categorías",
                    modifier = Modifier.padding(paddingValues)
                )

                AppScreen.Settings -> PlaceholderScreen(
                    "Ajustes",
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }
}