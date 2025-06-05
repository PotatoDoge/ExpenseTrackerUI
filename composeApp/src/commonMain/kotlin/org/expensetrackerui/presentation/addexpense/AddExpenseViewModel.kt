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
import org.expensetrackerui.domain.usecase.GetExpenseCategoriesUseCase
import org.expensetrackerui.domain.usecase.GetExpenseTagsUseCase
import org.expensetrackerui.domain.usecase.GetPaymentMethodsUseCase
import org.expensetrackerui.domain.usecase.SaveExpenseUseCase
import kotlinx.datetime.Clock // For Clock.System
import kotlinx.datetime.todayIn // This is an extension function, often needs explicit import if not imported by a wildcard
import org.expensetrackerui.data.model.Currency
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddExpenseViewModel(
    private val saveExpenseUseCase: SaveExpenseUseCase,
    private val getExpenseCategoriesUseCase: GetExpenseCategoriesUseCase,
    private val getExpenseTagsUseCase: GetExpenseTagsUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase
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
    var expenseLocation by mutableStateOf("")
        private set
    var selectedPaymentMethod by mutableStateOf<PaymentMethod?>(null)
        private set
    var selectedCategory by mutableStateOf<ExpenseCategory?>(null)
        private set
    var selectedTags by mutableStateOf(emptyList<ExpenseTag>())
        private set

    val categories: List<ExpenseCategory> = getExpenseCategoriesUseCase()
    val tags: List<ExpenseTag> = getExpenseTagsUseCase()
    val paymentMethods: List<PaymentMethod> = getPaymentMethodsUseCase()

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

    fun onExpenseLocationChanged(location: String) {
        expenseLocation = location
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
            location = expenseLocation.takeIf { it.isNotBlank() },
            paymentMethod = selectedPaymentMethod ?: PaymentMethod.OTHER, // Default if not selected
            category = selectedCategory!!, // Guaranteed to be non-null by validation
            tags = selectedTags
        )

        viewModelScope.launch {
            try {
                saveExpenseUseCase(newExpense)
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
        expenseLocation = ""
        selectedPaymentMethod = null // Clear selection
        selectedCategory = null // Clear selection
        selectedTags = emptyList() // Clear selection
    }
}