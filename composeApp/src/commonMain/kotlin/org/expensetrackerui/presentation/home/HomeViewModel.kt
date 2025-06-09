package org.expensetrackerui.presentation.home

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.cancel
import org.expensetrackerui.data.model.BudgetSummary
import org.expensetrackerui.data.model.FinancialTip
import org.expensetrackerui.data.model.SpendingItem
import org.expensetrackerui.data.model.Transaction
import org.expensetrackerui.data.repository.BudgetRepository
import org.expensetrackerui.data.repository.FinancialTipsRepository
import org.expensetrackerui.data.repository.SpendingRepository
import org.expensetrackerui.data.repository.TransactionRepository

class HomeViewModel(
    private val budgetRepository: BudgetRepository,
    private val spendingRepository: SpendingRepository,
    private val transactionRepository: TransactionRepository,
    private val financialTipsRepository: FinancialTipsRepository
) {

    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _budgetSummary = MutableStateFlow(BudgetSummary(0.0, 0.0))
    val budgetSummary: StateFlow<BudgetSummary> = _budgetSummary.asStateFlow()

    private val _paymentMethodSpending = MutableStateFlow<List<SpendingItem>>(emptyList())
    val paymentMethodSpending: StateFlow<List<SpendingItem>> = _paymentMethodSpending.asStateFlow()

    private val _categorySpending = MutableStateFlow<List<SpendingItem>>(emptyList())
    val categorySpending: StateFlow<List<SpendingItem>> = _categorySpending.asStateFlow()

    private val _recentTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val recentTransactions: StateFlow<List<Transaction>> = _recentTransactions.asStateFlow()

    private val _financialTips = MutableStateFlow<List<FinancialTip>>(emptyList())
    val financialTips: StateFlow<List<FinancialTip>> = _financialTips.asStateFlow()

    fun initialize() {
        loadHomeScreenData()
    }

    private fun loadHomeScreenData() {
        _budgetSummary.value = budgetRepository.getBudgetSummary()
        _paymentMethodSpending.value = (spendingRepository).getMappedPaymentMethodSpending()
        _categorySpending.value = (spendingRepository).getMappedCategorySpending()
        _recentTransactions.value = transactionRepository.getRecentTransactions()
        _financialTips.value = financialTipsRepository.getFinancialTips()
    }

    fun onCleared() {
        viewModelScope.cancel()
    }
}