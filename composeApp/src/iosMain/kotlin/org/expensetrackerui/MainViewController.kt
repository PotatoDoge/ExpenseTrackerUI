package org.expensetrackerui

import androidx.compose.ui.window.ComposeUIViewController
import org.expensetrackerui.di.initKoin
import org.expensetrackerui.presentation.MainViewModel
import org.expensetrackerui.presentation.home.HomeViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import androidx.compose.runtime.remember
import androidx.compose.runtime.DisposableEffect

class IosMainViewModelHolder : KoinComponent {
    val mainViewModel: MainViewModel by inject()
}

class IosHomeViewModelHolder : KoinComponent {
    val homeViewModel: HomeViewModel by inject()
}

fun MainViewController() = ComposeUIViewController {
    remember { initKoin() }

    val mainViewModel = remember { IosMainViewModelHolder().mainViewModel }
    val homeViewModel = remember { IosHomeViewModelHolder().homeViewModel }

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

    App(mainViewModel = mainViewModel, homeViewModel = homeViewModel)
}