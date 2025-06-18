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

    // --- Staging areas for filters (what the user has selected but not yet applied) ---
    private val _stagedSelectedCategories = MutableStateFlow(emptySet<ExpenseCategory>())
    val stagedSelectedCategories: StateFlow<Set<ExpenseCategory>> = _stagedSelectedCategories.asStateFlow()

    private val _stagedSelectedPaymentMethods = MutableStateFlow(emptySet<PaymentMethod>())
    val stagedSelectedPaymentMethods: StateFlow<Set<PaymentMethod>> = _stagedSelectedPaymentMethods.asStateFlow()

    private val _stagedSelectedTags = MutableStateFlow(emptySet<String>())
    val stagedSelectedTags: StateFlow<Set<String>> = _stagedSelectedTags.asStateFlow()

    private val _stagedStartDate = MutableStateFlow<LocalDate?>(null)
    val stagedStartDate: StateFlow<LocalDate?> = _stagedStartDate.asStateFlow()

    private val _stagedEndDate = MutableStateFlow<LocalDate?>(null)
    val stagedEndDate: StateFlow<LocalDate?> = _stagedEndDate.asStateFlow()

    // --- Currently active filters (what is actually being applied to the data) ---
    private val _activeSelectedCategories = MutableStateFlow(emptySet<ExpenseCategory>())
    val activeSelectedCategories: StateFlow<Set<ExpenseCategory>> = _activeSelectedCategories.asStateFlow()

    private val _activeSelectedPaymentMethods = MutableStateFlow(emptySet<PaymentMethod>())
    val activeSelectedPaymentMethods: StateFlow<Set<PaymentMethod>> = _activeSelectedPaymentMethods.asStateFlow()

    private val _activeSelectedTags = MutableStateFlow(emptySet<String>())
    val activeSelectedTags: StateFlow<Set<String>> = _activeSelectedTags.asStateFlow()

    private val _activeStartDate = MutableStateFlow<LocalDate?>(null)
    val activeStartDate: StateFlow<LocalDate?> = _activeStartDate.asStateFlow()

    private val _activeEndDate = MutableStateFlow<LocalDate?>(null)
    val activeEndDate: StateFlow<LocalDate?> = _activeEndDate.asStateFlow()


    // --- Functions to toggle staged filters (these do not immediately re-filter) ---
    fun toggleCategoryFilter(category: ExpenseCategory) {
        _stagedSelectedCategories.update { current ->
            if (current.contains(category)) current - category else current + category
        }
    }

    fun togglePaymentMethodFilter(method: PaymentMethod) {
        _stagedSelectedPaymentMethods.update { current ->
            if (current.contains(method)) current - method else current + method
        }
    }

    fun toggleTagFilter(tag: String) {
        _stagedSelectedTags.update { current ->
            if (current.contains(tag)) current - tag else current + tag
        }
    }

    fun setStagedStartDate(date: LocalDate?) {
        _stagedStartDate.value = date
        // Adjust end date if start date is after current end date
        if (date != null && _stagedEndDate.value != null && _stagedEndDate.value!! < date) {
            _stagedEndDate.value = date
        }
    }

    fun setStagedEndDate(date: LocalDate?) {
        _stagedEndDate.value = date
        // Adjust start date if end date is before current start date
        if (date != null && _stagedStartDate.value != null && _stagedStartDate.value!! > date) {
            _stagedStartDate.value = date
        }
    }

    fun clearStagedDateRangeFilter() {
        _stagedStartDate.value = null
        _stagedEndDate.value = null
    }

    /**
     * Applies the currently staged filters to the active filters, triggering a re-filtering
     * of the expenses. This should be called when the "Apply Filters" button is clicked.
     */
    fun applyFilters() {
        _activeSelectedCategories.value = _stagedSelectedCategories.value
        _activeSelectedPaymentMethods.value = _stagedSelectedPaymentMethods.value
        _activeSelectedTags.value = _stagedSelectedTags.value
        _activeStartDate.value = _stagedStartDate.value
        _activeEndDate.value = _stagedEndDate.value
        // The expensesGroupedByDate flow will automatically react to these changes
    }

    /**
     * Resets all staged filters to their default empty state.
     */
    fun resetStagedFilters() {
        _stagedSelectedCategories.value = emptySet()
        _stagedSelectedPaymentMethods.value = emptySet()
        _stagedSelectedTags.value = emptySet()
        _stagedStartDate.value = null
        _stagedEndDate.value = null
        // Optionally, you might want to immediately apply these cleared filters
        // For a "Clear All" button that instantly clears, call applyFilters() here:
        // applyFilters()
    }

    /**
     * Clears all staged filters and then immediately applies them.
     * This is useful for a "Clear All Filters" button.
     */
    fun clearAndApplyAllFilters() {
        resetStagedFilters() // Clear the staged filters
        applyFilters()       // Apply the now-empty staged filters
    }


    // Combined Flow for filtered expenses (uses active filters)
    val expensesGroupedByDate: StateFlow<Map<LocalDate, List<Expense>>> =
        combine(
            _allExpenses,
            _activeSelectedCategories, // Use active filters here
            _activeSelectedPaymentMethods, // Use active filters here
            _activeSelectedTags, // Use active filters here
            _activeStartDate, // Use active filters here
            _activeEndDate // Use active filters here
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
        // Initialize staged filters with the current active filters when the screen loads
        _stagedSelectedCategories.value = _activeSelectedCategories.value
        _stagedSelectedPaymentMethods.value = _activeSelectedPaymentMethods.value
        _stagedSelectedTags.value = _activeSelectedTags.value
        _stagedStartDate.value = _activeStartDate.value
        _stagedEndDate.value = _activeEndDate.value
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