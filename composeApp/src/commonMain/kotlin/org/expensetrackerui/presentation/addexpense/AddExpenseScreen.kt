package org.expensetrackerui.presentation.addexpense

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow // Para el selector de color
import androidx.compose.foundation.lazy.items // Para el selector de color
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape // Para los selectores de color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check // Para indicar color seleccionado
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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.expensetrackerui.data.model.Currency
import org.expensetrackerui.data.model.TagWithColor
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.luminance

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddExpenseScreen(
    onClose: () -> Unit,
    viewModel: AddExpenseViewModel,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val tagInput by viewModel.tagInput.collectAsState()
    val currentTags by viewModel.currentTags.collectAsState()
    val selectedTagColor by viewModel.selectedTagColor.collectAsState() // Nuevo: Color seleccionado

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            androidx.compose.material3.IconButton(
                onClick = onClose,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Nuevo gasto",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center)
            )
            androidx.compose.material3.IconButton(
                onClick = {
                    viewModel.clearAllInputs()
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Limpiar campos",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        ExpenseInputField(
            value = viewModel.expenseName,
            onValueChange = viewModel::onExpenseNameChanged,
            placeholder = "Nombre",
        )

        ExpenseInputField(
            value = viewModel.expenseAmount,
            onValueChange = viewModel::onExpenseAmountChanged,
            placeholder = "Cantidad",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Box(modifier = Modifier.fillMaxWidth()) {
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
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                currencyOptions.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name, color = MaterialTheme.colorScheme.onSurface) },
                        onClick = {
                            viewModel.onCurrencySelected(type)
                            showCurrencyDropdown = false
                        }
                    )
                }
            }
        }

        var showDatePicker by remember { mutableStateOf(false) }
        ExpenseDropdownField(
            value = viewModel.expenseDate.toString(),
            placeholder = "Fecha",
            onClick = { showDatePicker = true },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Seleccionar fecha",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        )

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = viewModel.expenseDate.toEpochDays() * 24L * 60 * 60 * 1000L
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            viewModel.onExpenseDateChanged(LocalDate.fromEpochDays((millis / (24L * 60 * 60 * 1000L)).toInt()))
                        }
                        showDatePicker = false
                    }) {
                        Text("OK", color = MaterialTheme.colorScheme.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            var showPaymentMethodDropdown by remember { mutableStateOf(false) }
            val selectedMethodText =
                viewModel.selectedPaymentMethod?.name?.replace("_", " ")?.lowercase()
                    ?.replaceFirstChar { it.uppercase() } ?: ""

            ExpenseDropdownField(
                value = selectedMethodText,
                placeholder = "Método de Pago",
                onClick = { showPaymentMethodDropdown = true }
            )
            DropdownMenu(
                expanded = showPaymentMethodDropdown,
                onDismissRequest = { showPaymentMethodDropdown = false },
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                viewModel.paymentMethods.forEach { method ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                method.name.replace("_", " ").lowercase()
                                    .replaceFirstChar { it.uppercase() },
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            viewModel.onPaymentMethodSelected(method)
                            showPaymentMethodDropdown = false
                        }
                    )
                }
            }
        }

        Text(
            text = "Categoría",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            viewModel.categories.forEach { category ->
                SelectableChip(
                    text = category.name.replace("_", " ").lowercase().replaceFirstChar { it.uppercase() },
                    isSelected = viewModel.selectedCategory == category,
                    onClick = { viewModel.onCategorySelected(category) }
                )
            }
        }

        Text(
            text = "Etiquetas",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )

        // Selector de color para las etiquetas
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SuggestedTagColors.colors) { color ->
                ColorOption(
                    color = color,
                    isSelected = color == selectedTagColor,
                    onClick = { viewModel.onTagColorSelected(color) }
                )
            }
        }
        TextField(
            value = tagInput,
            onValueChange = viewModel::onTagInputChanged,
            placeholder = { Text("Escribe una etiqueta...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                errorContainerColor = MaterialTheme.colorScheme.errorContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface
            ),
            textStyle = LocalTextStyle.current.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.addTag(tagInput, selectedTagColor) { errorMessage -> // Pasa el color aquí
                        scope.launch { snackbarHostState.showSnackbar(errorMessage) }
                    }
                }
            ),
            trailingIcon = {
                androidx.compose.material3.IconButton(
                    onClick = {
                        viewModel.addTag(tagInput, selectedTagColor) { errorMessage -> // Pasa el color aquí
                            scope.launch { snackbarHostState.showSnackbar(errorMessage) }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar etiqueta",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            singleLine = true
        )

        if (currentTags.isNotEmpty()) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                currentTags.forEach { tagWithColor -> // Iterar sobre TagWithColor
                    RemovableTagChip(
                        tagWithColor = tagWithColor, // Pasar el objeto completo
                        onRemove = { viewModel.removeTag(tagWithColor.name) } // Eliminar por nombre
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.saveExpense(
                    onSuccess = {
                        scope.launch { snackbarHostState.showSnackbar("Gasto guardado exitosamente!") }
                        onClose()
                    },
                    onError = { message ->
                        scope.launch { snackbarHostState.showSnackbar(message) }
                    }
                )
            },
            enabled = viewModel.isSaveButtonEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = "Guardar",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// Reusable Composables (ExpenseInputField, ExpenseDropdownField, SelectableChip) remain the same

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
        placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant) },
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp),
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            errorContainerColor = MaterialTheme.colorScheme.errorContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        textStyle = LocalTextStyle.current.copy(
            fontWeight = FontWeight.Normal,
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
            .height(56.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
            .border(
                1.dp,
                Color.Transparent,
                RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (leadingIcon != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    leadingIcon()
                    Spacer(Modifier.width(12.dp))
                }
            }

            if (value.isNotEmpty()) {
                Text(
                    text = value,
                    style = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Text(
                    text = placeholder,
                    style = LocalTextStyle.current.copy(
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.weight(1f)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Dropdown",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
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
    androidx.compose.material3.Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 1.dp
    ) {
        Text(
            text = text,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
    }
}

@Composable
fun RemovableTagChip(
    tagWithColor: TagWithColor, // Ahora recibe un objeto TagWithColor
    onRemove: (String) -> Unit
) {
    androidx.compose.material3.Surface(
        modifier = Modifier,
        shape = RoundedCornerShape(8.dp),
        color = tagWithColor.color, // Usar el color de la etiqueta
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = tagWithColor.name,
                // Calcular un color de texto contrastante
                color = if (tagWithColor.color.luminance() > 0.5f) Color.Black else Color.White,
                fontSize = 14.sp
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Eliminar etiqueta",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onRemove(tagWithColor.name) },
                // Calcular un color de ícono contrastante
                tint = if (tagWithColor.color.luminance() > 0.5f) Color.Black.copy(alpha = 0.7f) else Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ColorOption(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(32.dp) // Tamaño del círculo de color
            .clip(CircleShape)
            .background(color)
            .border(
                width = 2.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Color seleccionado",
                tint = if (color.luminance() > 0.5f) Color.Black else Color.White, // Color del check
                modifier = Modifier.size(20.dp)
            )
        }
    }
}