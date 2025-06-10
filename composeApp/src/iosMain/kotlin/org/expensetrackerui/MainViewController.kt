package org.expensetrackerui

import androidx.compose.ui.window.ComposeUIViewController
import org.expensetrackerui.di.initKoin
import org.expensetrackerui.presentation.MainViewModel
import org.expensetrackerui.presentation.home.HomeViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import androidx.compose.runtime.remember
import androidx.compose.runtime.DisposableEffect
import org.expensetrackerui.presentation.addexpense.AddExpenseViewModel
import org.expensetrackerui.theme.ExpenseTrackerUITheme

class IosMainViewModelHolder : KoinComponent {
    val mainViewModel: MainViewModel by inject()
}

class IosHomeViewModelHolder : KoinComponent {
    val homeViewModel: HomeViewModel by inject()
}

class IosAddExpenseViewModelHolder: KoinComponent {
    val addExpenseViewModel: AddExpenseViewModel by inject()
}

fun MainViewController() = ComposeUIViewController {
    remember { initKoin() }

    val darkTheme = isSystemInDarkThemeKmp()

    val mainViewModel = remember { IosMainViewModelHolder().mainViewModel }
    val homeViewModel = remember { IosHomeViewModelHolder().homeViewModel }
    val addExpenseViewModel = remember { IosAddExpenseViewModelHolder().addExpenseViewModel }

    DisposableEffect(mainViewModel) {
        onDispose {
            mainViewModel.onCleared()
        }
    }
    DisposableEffect(homeViewModel) {
        onDispose {
            homeViewModel.onCleared()
        }
    }

    ExpenseTrackerUITheme(
        darkTheme = darkTheme
    ) {
        App(
            mainViewModel = mainViewModel,
            homeViewModel = homeViewModel,
            addExpenseViewModel = addExpenseViewModel
        )
    }}