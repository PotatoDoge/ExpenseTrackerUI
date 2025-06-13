package org.expensetrackerui.presentation.showexpenses

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.expensetrackerui.data.model.Expense
import org.expensetrackerui.data.model.ExpenseCategory
import org.expensetrackerui.data.model.PaymentMethod
import org.expensetrackerui.data.repository.ExpenseRepository

class ShowExpensesViewModel(
    private val expenseRepository: ExpenseRepository
) {
    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val _allExpenses = MutableStateFlow<List<Expense>>(emptyList())
    val allExpenses: StateFlow<List<Expense>> = _allExpenses

    // --- Filter Options ---
    val availableCategories: StateFlow<List<ExpenseCategory>> =
        MutableStateFlow(ExpenseCategory.entries.toList())
    val availablePaymentMethods: StateFlow<List<PaymentMethod>> =
        MutableStateFlow(PaymentMethod.entries.toList())
    val availableTags: StateFlow<List<String>> = _allExpenses.map { expenses ->
        expenses.flatMap { it.tags.map { tag -> tag.name } }.distinct().sorted()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    // --- Selected Filters ---
    private val _selectedCategories = MutableStateFlow(emptySet<ExpenseCategory>())
    val selectedCategories: StateFlow<Set<ExpenseCategory>> = _selectedCategories.asStateFlow()

    private val _selectedPaymentMethods = MutableStateFlow(emptySet<PaymentMethod>())
    val selectedPaymentMethods: StateFlow<Set<PaymentMethod>> =
        _selectedPaymentMethods.asStateFlow()

    private val _selectedTags = MutableStateFlow(emptySet<String>())
    val selectedTags: StateFlow<Set<String>> = _selectedTags.asStateFlow()

    // --- Date Range Filters ---
    private val _startDate = MutableStateFlow<LocalDate?>(null)
    val startDate: StateFlow<LocalDate?> = _startDate.asStateFlow()

    private val _endDate = MutableStateFlow<LocalDate?>(null)
    val endDate: StateFlow<LocalDate?> = _endDate.asStateFlow()


    // --- Functions to toggle selected filters ---
    fun toggleCategoryFilter(category: ExpenseCategory) {
        _selectedCategories.update { current ->
            if (current.contains(category)) current - category else current + category
        }
    }

    fun togglePaymentMethodFilter(method: PaymentMethod) {
        _selectedPaymentMethods.update { current ->
            if (current.contains(method)) current - method else current + method
        }
    }

    fun toggleTagFilter(tag: String) {
        _selectedTags.update { current ->
            if (current.contains(tag)) current - tag else current + tag
        }
    }

    fun setStartDate(date: LocalDate?) {
        _startDate.value = date
        if (date != null && _endDate.value != null && _endDate.value!! < date) {
            _endDate.value = date
        }
    }

    fun setEndDate(date: LocalDate?) {
        _endDate.value = date
        if (date != null && _startDate.value != null && _startDate.value!! > date) {
            _startDate.value = date
        }
    }

    fun clearDateRangeFilter() {
        _startDate.value = null
        _endDate.value = null
    }

    val expensesGroupedByDate: StateFlow<Map<LocalDate, List<Expense>>> =
        combine(
            _allExpenses,
            _selectedCategories,
            _selectedPaymentMethods,
            _selectedTags,
            _startDate,
            _endDate
        ) { args ->
            val expenses = args[0] as List<Expense>
            val categories = args[1] as Set<ExpenseCategory>
            val methods = args[2] as Set<PaymentMethod>
            val tags = args[3] as Set<String>
            val startDate = args[4] as LocalDate?
            val endDate = args[5] as LocalDate?

            expenses
                .filter { expense ->
                    val categoryMatches =
                        categories.isEmpty() || categories.contains(expense.category)
                    val methodMatches = methods.isEmpty() || methods.contains(expense.paymentMethod)
                    val tagsMatch = tags.isEmpty() || expense.tags.any { tagWithColor ->
                        tags.contains(tagWithColor.name)
                    }

                    val dateMatches = if (startDate == null && endDate == null) {
                        true
                    } else {
                        val expenseDate = expense.date
                        val startsAfterOrOn = startDate == null || expenseDate >= startDate
                        val endsBeforeOrOn = endDate == null || expenseDate <= endDate
                        startsAfterOrOn && endsBeforeOrOn
                    }

                    categoryMatches && methodMatches && tagsMatch && dateMatches
                }
                .groupBy { it.date }
                .entries
                .sortedByDescending { it.key }
                .associate { it.key to it.value }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyMap()
        )

    fun initialize() {
        loadAllExpenses()
    }

    private fun loadAllExpenses() {
        viewModelScope.launch {
            expenseRepository.getExpenses().collect { expenses ->
                _allExpenses.value = expenses
            }
        }
    }

    fun onCleared() {
        viewModelScope.cancel()
    }
}