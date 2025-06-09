package org.expensetrackerui.presentation.addexpense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import org.expensetrackerui.data.model.Expense
import org.expensetrackerui.data.model.ExpenseCategory
import org.expensetrackerui.data.model.ExpenseTag
import org.expensetrackerui.data.model.PaymentMethod
import kotlinx.datetime.Clock // For Clock.System
import kotlinx.datetime.todayIn // This is an extension function, often needs explicit import if not imported by a wildcard
import org.expensetrackerui.data.model.Currency
import org.expensetrackerui.data.repository.GetExpenseCategoriesRepository
import org.expensetrackerui.data.repository.GetExpenseTagsRepository
import org.expensetrackerui.data.repository.GetPaymentMethodsRepository
import org.expensetrackerui.data.repository.SaveExpenseRepository
import org.expensetrackerui.util.preview_dummies.DummyGetPaymentMethodsUseCase
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddExpenseViewModel(
    private val saveExpenseRepository: SaveExpenseRepository,
    private val getExpenseCategoriesRepository: GetExpenseCategoriesRepository,
    private val getExpenseTagsRepository: GetExpenseTagsRepository,
    private val getPaymentMethodsRepository: GetPaymentMethodsRepository
) : ViewModel() {

    // UI State
    var expenseName by mutableStateOf("")
        private set
    var expenseAmount by mutableStateOf("")
        private set
    var selectedCurrency by mutableStateOf(Currency.MXN)
        private set
    var expenseDate by mutableStateOf(Clock.System.todayIn(TimeZone.currentSystemDefault()))
        private set
    var selectedPaymentMethod by mutableStateOf<PaymentMethod?>(null)
        private set
    var selectedCategory by mutableStateOf<ExpenseCategory?>(null)
        private set
    var selectedTags by mutableStateOf(emptyList<ExpenseTag>())
        private set

    val categories: List<ExpenseCategory> = getExpenseCategoriesRepository.invoke()
    val tags: List<ExpenseTag> = getExpenseTagsRepository.invoke()
    val paymentMethods: List<PaymentMethod> = getPaymentMethodsRepository.invoke()

    val isSaveButtonEnabled: Boolean
        get() = expenseName.isNotBlank() &&
                expenseAmount.toDoubleOrNull() != null &&
                selectedCategory != null

    fun onExpenseNameChanged(name: String) {
        expenseName = name
    }

    fun onExpenseAmountChanged(amount: String) {
        // Basic validation for numeric input
        if (amount.isEmpty() || amount.matches(Regex("^\\d*\\.?\\d*\$"))) {
            expenseAmount = amount
        }
    }

    fun onExpenseDateChanged(date: LocalDate) {
        expenseDate = date
    }

    fun onPaymentMethodSelected(method: PaymentMethod) {
        selectedPaymentMethod = method
    }

    fun onCurrencySelected(currency: Currency) {
        selectedCurrency = currency
    }

    fun onCategorySelected(category: ExpenseCategory) {
        selectedCategory = category
    }

    fun onTagToggled(tag: ExpenseTag) {
        selectedTags = if (selectedTags.contains(tag)) {
            selectedTags - tag
        } else {
            selectedTags + tag
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun saveExpense(onSuccess: () -> Unit, onError: (String) -> Unit) {
        if (!isSaveButtonEnabled) {
            onError("Please fill all required fields.")
            return
        }

        val amountDouble = expenseAmount.toDoubleOrNull() ?: run {
            onError("Invalid amount.")
            return
        }
        val id = Uuid.random().toString();

        val newExpense = Expense(
            id = id,
            name = expenseName,
            amount = amountDouble,
            currency = selectedCurrency, // Using a default type for now
            date = expenseDate,
            paymentMethod = selectedPaymentMethod ?: PaymentMethod.OTHER, // Default if not selected
            category = selectedCategory!!, // Guaranteed to be non-null by validation
            tags = selectedTags
        )

        viewModelScope.launch {
            try {
                saveExpenseRepository.invoke(newExpense)
                onSuccess()
            } catch (e: Exception) {
                onError("Failed to save expense: ${e.message}")
            }
        }
    }

    fun clearAllInputs() {
        expenseName = ""
        expenseAmount = ""
        selectedCurrency = Currency.MXN
        expenseDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        selectedPaymentMethod = null // Clear selection
        selectedCategory = null // Clear selection
        selectedTags = emptyList() // Clear selection
    }
}