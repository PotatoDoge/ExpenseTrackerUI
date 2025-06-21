package org.expensetrackerui.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.expensetrackerui.data.model.AppScreen
import org.expensetrackerui.data.model.Expense


class MainViewModel : ViewModel() {

    private val _currentScreen: MutableStateFlow<AppScreen> = MutableStateFlow(AppScreen.Home)
    val currentScreen: StateFlow<AppScreen> = _currentScreen

    private val _selectedExpenseForDetail = MutableStateFlow<Expense?>(null)
    val selectedExpenseForDetail: StateFlow<Expense?> = _selectedExpenseForDetail

    private val _showExpenseDetail = MutableStateFlow(false)
    val showExpenseDetail: StateFlow<Boolean> = _showExpenseDetail

    fun selectScreen(screen: AppScreen) {
        _currentScreen.value = screen
        if (screen != AppScreen.ExpensesList) {
            hideExpenseDetail()
        }
    }

    fun showExpenseDetail(expense: Expense) {
        _selectedExpenseForDetail.value = expense
        _showExpenseDetail.value = true
    }

    fun hideExpenseDetail() {
        _showExpenseDetail.value = false
        _selectedExpenseForDetail.value = null
    }
}