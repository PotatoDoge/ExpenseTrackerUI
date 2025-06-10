package org.expensetrackerui.presentation.addexpense

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import org.expensetrackerui.data.model.Expense
import org.expensetrackerui.data.model.ExpenseCategory
import org.expensetrackerui.data.model.ExpenseTag
import org.expensetrackerui.data.model.PaymentMethod
import kotlinx.datetime.Clock
import kotlinx.datetime.todayIn
import org.expensetrackerui.data.model.Currency
import org.expensetrackerui.data.model.TagWithColor
import org.expensetrackerui.data.repository.GetExpenseCategoriesRepository
import org.expensetrackerui.data.repository.GetExpenseTagsRepository
import org.expensetrackerui.data.repository.GetPaymentMethodsRepository
import org.expensetrackerui.data.repository.SaveExpenseRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddExpenseViewModel(
    private val saveExpenseRepository: SaveExpenseRepository,
    private val getExpenseCategoriesRepository: GetExpenseCategoriesRepository,
    private val getExpenseTagsRepository: GetExpenseTagsRepository,
    private val getPaymentMethodsRepository: GetPaymentMethodsRepository
) : ViewModel() {

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

    private val _tagInput = MutableStateFlow("")
    val tagInput: StateFlow<String> = _tagInput.asStateFlow()

    // Now stores TagWithColor objects
    private val _currentTags = MutableStateFlow<List<TagWithColor>>(emptyList())
    val currentTags: StateFlow<List<TagWithColor>> = _currentTags.asStateFlow()

    private val _selectedTagColor = MutableStateFlow(SuggestedTagColors.colors.first())
    val selectedTagColor: StateFlow<Color> = _selectedTagColor.asStateFlow()

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

    fun addTag(tag: String, color: Color, onError: (String) -> Unit) {
        val trimmedTag = tag.trim()

        if (trimmedTag.isBlank()) {
            onError("La etiqueta no puede estar vacía.")
            return
        }
        if (trimmedTag.length > 20) {
            onError("La etiqueta no puede exceder los 20 caracteres.")
            return
        }
        val lowerCaseTag = trimmedTag.lowercase()
        if (_currentTags.value.any { it.name.lowercase() == lowerCaseTag }) {
            onError("La etiqueta '$trimmedTag' ya existe.")
            return
        }

        _currentTags.value = _currentTags.value + TagWithColor(trimmedTag, color)
        _tagInput.value = ""
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
            currency = selectedCurrency,
            date = expenseDate,
            paymentMethod = selectedPaymentMethod ?: PaymentMethod.OTHER,
            category = selectedCategory!!,
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

    fun onTagInputChanged(input: String) {
        _tagInput.value = input
    }

    fun removeTag(tag: String) {
        _currentTags.value = _currentTags.value.filter { it.name != tag }
    }

    fun onTagColorSelected(color: Color) {
        _selectedTagColor.value = color
    }

    fun clearAllInputs() {
        expenseName = ""
        expenseAmount = ""
        selectedCurrency = Currency.MXN
        expenseDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        selectedPaymentMethod = null
        selectedCategory = null
        selectedTags = emptyList()

        _tagInput.value = ""
        _currentTags.value = emptyList()
        _selectedTagColor.value = SuggestedTagColors.colors.first()
    }
}

object SuggestedTagColors {
    val colors = listOf(
        Color(0xFFE57373),
        Color(0xFF81C784),
        Color(0xFF64B5F6),
        Color(0xFFFFD54F),
        Color(0xFFBA68C8),
        Color(0xFF90A4AE),
        Color(0xFFFFB74D)
    )
}