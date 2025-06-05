package org.expensetrackerui.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.expensetrackerui.data.model.AppScreen

class MainViewModel : ViewModel() {
    var currentScreen: AppScreen by mutableStateOf(AppScreen.Home)
        private set

    fun selectScreen(screen: AppScreen) {
        currentScreen = screen
    }
}