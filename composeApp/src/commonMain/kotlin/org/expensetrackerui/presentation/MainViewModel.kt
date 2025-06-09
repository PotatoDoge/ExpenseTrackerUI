package org.expensetrackerui.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import org.expensetrackerui.data.model.AppScreen


class MainViewModel {
    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val _currentScreen: MutableStateFlow<AppScreen> = MutableStateFlow(AppScreen.Home)

    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    fun selectScreen(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun onCleared() {
        viewModelScope.cancel()
    }
}