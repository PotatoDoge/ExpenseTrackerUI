package org.expensetrackerui.presentation.addexpense

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.expensetrackerui.data.model.Currency
import org.expensetrackerui.data.source.LocalExpenseDataSource
import org.expensetrackerui.domain.usecase.GetExpenseCategoriesUseCase
import org.expensetrackerui.domain.usecase.GetExpenseTagsUseCase
import org.expensetrackerui.domain.usecase.GetPaymentMethodsUseCase
import org.expensetrackerui.domain.usecase.SaveExpenseUseCase

// Helper function to create the ViewModel (can be done with dependency injection framework)
@Composable
fun rememberAddExpenseViewModel(): AddExpenseViewModel {
    val saveExpenseUseCase = remember { SaveExpenseUseCase(LocalExpenseDataSource()) }
    val getExpenseCategoriesUseCase = remember { GetExpenseCategoriesUseCase() }
    val getExpenseTagsUseCase = remember { GetExpenseTagsUseCase() }
    val getPaymentMethodsUseCase = remember { GetPaymentMethodsUseCase() }
    return remember {
        AddExpenseViewModel(
            saveExpenseUseCase,
            getExpenseCategoriesUseCase,
            getExpenseTagsUseCase,
            getPaymentMethodsUseCase
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    onClose: () -> Unit,
    viewModel: AddExpenseViewModel = rememberAddExpenseViewModel(),
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp), // Uniform spacing
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Header ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cerrar"
                    )
                }
                Text(
                    text = "Nuevo gasto",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = {
                        viewModel.clearAllInputs()
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Limpiar campos"
                    )
                }
            }

            // --- Input Fields ---
            ExpenseInputField(
                value = viewModel.expenseName,
                onValueChange = viewModel::onExpenseNameChanged,
                placeholder = "Nombre del gasto"
            )

            ExpenseInputField(
                value = viewModel.expenseAmount,
                onValueChange = viewModel::onExpenseAmountChanged,
                placeholder = "Cantidad",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            var showCurrencyDropdown by remember { mutableStateOf(false) }
            val currencyOptions = Currency.entries.toList()
            ExpenseDropdownField(
                value = viewModel.selectedCurrency.name,
                placeholder = "Moneda",
                onClick = { showCurrencyDropdown = true }
            )
            DropdownMenu(
                expanded = showCurrencyDropdown,
                onDismissRequest = { showCurrencyDropdown = false },
                modifier = Modifier.fillMaxWidth(0.9f) // Adjust width as needed
            ) {
                currencyOptions.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            viewModel.onCurrencySelected(type)
                            showCurrencyDropdown = false
                        }
                    )
                }
            }


            // Date Picker
            var showDatePicker by remember { mutableStateOf(false) }
            ExpenseDropdownField(
                value = viewModel.expenseDate.toString(),
                placeholder = "Fecha",
                onClick = { showDatePicker = true },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Seleccionar fecha"
                    )
                }
            )

            if (showDatePicker) {
                // This is a basic Material3 DatePicker. You might need to use a custom dialog
                // or a library for more advanced cross-platform date pickers.
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = viewModel.expenseDate.toEpochDays() * 24L * 60 * 60 * 1000L
                )
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                // Convert millis to LocalDate, careful with timezones
                                viewModel.onExpenseDateChanged(LocalDate.fromEpochDays((millis / (24L * 60 * 60 * 1000L)).toInt()))
                            }
                            showDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancelar")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }


            ExpenseInputField(
                value = viewModel.expenseLocation,
                onValueChange = viewModel::onExpenseLocationChanged,
                placeholder = "Lugar (opcional)"
            )

            // Payment Method Dropdown
            var showPaymentMethodDropdown by remember { mutableStateOf(false) }
            val selectedMethodText =
                viewModel.selectedPaymentMethod?.name?.replace("_", " ")?.lowercase()
                    ?.replaceFirstChar { it.uppercase() } ?: ""

            ExpenseDropdownField(
                value = selectedMethodText,
                placeholder = "Método de pago",
                onClick = { showPaymentMethodDropdown = true }
            )
            DropdownMenu(
                expanded = showPaymentMethodDropdown,
                onDismissRequest = { showPaymentMethodDropdown = false },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                viewModel.paymentMethods.forEach { method ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                method.name.replace("_", " ").lowercase()
                                    .replaceFirstChar { it.uppercase() })
                        },
                        onClick = {
                            viewModel.onPaymentMethodSelected(method)
                            showPaymentMethodDropdown = false
                        }
                    )
                }
            }


            // --- Category Selection ---
            Text(
                text = "Categoría",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.categories.forEach { category ->
                    SelectableChip(
                        text = category.name.lowercase().replaceFirstChar { it.uppercase() },
                        isSelected = viewModel.selectedCategory == category,
                        onClick = { viewModel.onCategorySelected(category) }
                    )
                }
            }

            // --- Tags Selection ---
            Text(
                text = "Etiquetas",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                viewModel.tags.forEach { tag ->
                    SelectableChip(
                        text = tag.name.lowercase().replaceFirstChar { it.uppercase() },
                        isSelected = viewModel.selectedTags.contains(tag),
                        onClick = { viewModel.onTagToggled(tag) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Action Button ---
            Button(
                onClick = {
                    viewModel.saveExpense(
                        onSuccess = {
                            scope.launch { snackBarHostState.showSnackbar("Gasto guardado exitosamente!") }
                            onClose()
                        },
                        onError = { message ->
                            scope.launch { snackBarHostState.showSnackbar(message) }
                        }
                    )
                },
                enabled = viewModel.isSaveButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp), // Generous height
                shape = RoundedCornerShape(12.dp),
                colors = buttonColors(containerColor = Color(0xFF2563EB))
            ) {
                Text(
                    text = "Guardar",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun ExpenseInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray) },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp), // Uniform height
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF1F3F5),
            unfocusedContainerColor = Color(0xFFF1F3F5),
            disabledContainerColor = Color(0xFFF1F3F5),
            errorContainerColor = Color(0xFFF1F3F5),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        textStyle = LocalTextStyle.current.copy(
            fontWeight = FontWeight.Light,
            fontSize = 16.sp
        ),
        keyboardOptions = keyboardOptions,
        singleLine = true
    )
}

@Composable
fun ExpenseDropdownField(
    value: String,
    placeholder: String,
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) // Fixed height as per description
            .background(Color(0xFFF1F3F5), RoundedCornerShape(12.dp)) // Light gray background
            .border(
                1.dp,
                Color.Transparent,
                RoundedCornerShape(12.dp)
            ) // No visible border by default
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.CenterStart // Align content to the start
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp), // Standard horizontal padding for text fields
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // To push trailing icon to end
        ) {
            if (leadingIcon != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    leadingIcon()
                    Spacer(Modifier.width(12.dp)) // Space between icon and text
                }
            }

            // Display value or placeholder
            if (value.isNotEmpty()) {
                Text(
                    text = value,
                    style = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp
                    ),
                    color = Color.Black,
                    modifier = Modifier.weight(1f) // Allow text to take available space
                )
            } else {
                Text(
                    text = placeholder,
                    style = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp
                    ),
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Dropdown",
                tint = Color.Gray // Optional: tint the icon
            )
        }
    }
}


@Composable
fun SelectableChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) Color(0xFF2563EB) else Color(0xFFF1F3F5),
        shadowElevation = 1.dp
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
    }
}