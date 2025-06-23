package org.expensetrackerui.presentation

import androidx.compose.animation.core.MutableTransitionState // Import this
import androidx.compose.runtime.Composable // Needed for the helper composable
import androidx.compose.runtime.LaunchedEffect // Needed for the helper composable
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

    val expenseDetailTransitionState = MutableTransitionState(false) // false means initially hidden

    fun selectScreen(screen: AppScreen) {
        _currentScreen.value = screen
        if (screen != AppScreen.ExpensesList) {
            hideExpenseDetail()
        }
    }

    fun showExpenseDetail(expense: Expense) {
        _selectedExpenseForDetail.value = expense
        _showExpenseDetail.value = true
        expenseDetailTransitionState.targetState = true
    }

    fun hideExpenseDetail() {
        _showExpenseDetail.value = false
        expenseDetailTransitionState.targetState = false
    }

    /**
     * This Composable function should be called within the main App composable.
     * It observes the animation state and cleans up `_selectedExpenseForDetail`
     * when the exit animation is complete.
     */
    @Composable
    fun ObserveExpenseDetailAnimationAndCleanup() {
        LaunchedEffect(expenseDetailTransitionState.currentState, expenseDetailTransitionState.isIdle) {
            if (!expenseDetailTransitionState.targetState && expenseDetailTransitionState.isIdle) {
                _selectedExpenseForDetail.value = null
            }
        }
    }
}