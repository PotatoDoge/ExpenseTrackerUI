package org.expensetrackerui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.expensetrackerui.data.model.AppScreen
import org.expensetrackerui.presentation.AppBottomNavigationBar
import org.expensetrackerui.presentation.MainViewModel
import org.expensetrackerui.presentation.PlaceholderScreen
import org.expensetrackerui.presentation.addexpense.AddExpenseScreen
import org.expensetrackerui.presentation.addexpense.AddExpenseViewModel
import org.expensetrackerui.presentation.expensedetail.ExpenseDetailScreen
import org.expensetrackerui.presentation.home.HomeScreen
import org.expensetrackerui.presentation.home.HomeViewModel
import org.expensetrackerui.presentation.showexpenses.ShowAllExpensesScreen
import org.expensetrackerui.presentation.showexpenses.ShowExpensesViewModel
import org.expensetrackerui.util.preview_dummies.DummyBudgetRepository
import org.expensetrackerui.util.preview_dummies.DummyExpenseRepository
import org.expensetrackerui.util.preview_dummies.DummyFinancialTipsRepository
import org.expensetrackerui.util.preview_dummies.DummyGetExpenseCategoriesUseCase
import org.expensetrackerui.util.preview_dummies.DummyGetExpenseTagsUseCase
import org.expensetrackerui.util.preview_dummies.DummyGetPaymentMethodsUseCase
import org.expensetrackerui.util.preview_dummies.DummySaveExpenseUseCase
import org.expensetrackerui.util.preview_dummies.DummySpendingRepository
import org.expensetrackerui.util.preview_dummies.DummyTransactionRepository


@Composable
fun App(
    mainViewModel: MainViewModel = remember { MainViewModel() },
    homeViewModel: HomeViewModel = remember {
        HomeViewModel(
            budgetRepository = DummyBudgetRepository(),
            spendingRepository = DummySpendingRepository(),
            expenseRepository = DummyExpenseRepository(),
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
    },
    showExpensesViewModel: ShowExpensesViewModel = remember {
        ShowExpensesViewModel(
            expenseRepository = DummyExpenseRepository()
        )
    }
) {
    MaterialTheme {
        val currentScreen by mainViewModel.currentScreen.collectAsState()
        val selectedExpenseForDetail by mainViewModel.selectedExpenseForDetail.collectAsState()
        val expenseDetailTransitionState = mainViewModel.expenseDetailTransitionState
        mainViewModel.ObserveExpenseDetailAnimationAndCleanup()

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                val showNavbar by mainViewModel.showExpenseDetail.collectAsState()
                if (!showNavbar) {
                    AppBottomNavigationBar(
                        currentScreen = currentScreen,
                        onScreenSelected = { screen -> mainViewModel.selectScreen(screen) }
                    )
                }
            }
        ) { paddingValues ->
            when (currentScreen) {
                AppScreen.Home -> HomeScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = homeViewModel,
                    mainViewModel = mainViewModel
                )

                AppScreen.AddExpense -> AddExpenseScreen(
                    modifier = Modifier.padding(paddingValues),
                    onClose = {
                        mainViewModel.selectScreen(AppScreen.Home)
                    },
                    viewModel = addExpenseViewModel
                )

                AppScreen.ExpensesList -> ShowAllExpensesScreen(
                    modifier = Modifier.padding(paddingValues),
                    viewModel = showExpensesViewModel,
                    mainViewModel = mainViewModel
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

            AnimatedVisibility(
                visibleState = expenseDetailTransitionState,
                enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
                exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth })
            ) {
                selectedExpenseForDetail?.let { expense ->
                    ExpenseDetailScreen(
                        expense = expense,
                        onClose = { mainViewModel.hideExpenseDetail() },
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}