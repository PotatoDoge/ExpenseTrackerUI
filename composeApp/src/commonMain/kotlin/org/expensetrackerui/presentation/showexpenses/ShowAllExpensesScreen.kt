package org.expensetrackerui.presentation.showexpenses

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.expensetrackerui.presentation.TransactionListItem
import org.expensetrackerui.util.formatDateForDisplay

// Define FilterType (outside the composable for cleaner code)
sealed class FilterType {
    object Category : FilterType()
    object PaymentMethod : FilterType()
    object Tags : FilterType()
    object Period : FilterType()
}

// Data class to hold chip configuration (outside the composable)
data class FilterChipConfig(val text: String, val type: FilterType)

@OptIn(ExperimentalMaterial3Api::class) // For FilterChip, DropdownMenu, DatePicker
@Composable
fun ShowAllExpensesScreen(
    modifier: Modifier = Modifier,
    viewModel: ShowExpensesViewModel
) {
    DisposableEffect(viewModel) {
        viewModel.initialize()
        onDispose {
            viewModel.onCleared()
        }
    }

    val expensesByDate by viewModel.expensesGroupedByDate.collectAsState()

    // --- Filter UI State ---
    var openFilterMenuType by remember { mutableStateOf<FilterType?>(null) }

    // Collected filter states from ViewModel
    val selectedCategories by viewModel.selectedCategories.collectAsState()
    val selectedPaymentMethods by viewModel.selectedPaymentMethods.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()

    // Available filter options from ViewModel
    val availableCategories by viewModel.availableCategories.collectAsState()
    val availablePaymentMethods by viewModel.availablePaymentMethods.collectAsState()
    val availableTags by viewModel.availableTags.collectAsState()


    // --- Date Picker Dialog states ---
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .padding(
                horizontal = 24.dp,
                vertical = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = "Historial de Gastos",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // --- Scrollable Row of Filter Chips ---
        val filterChips = listOf(
            FilterChipConfig("Categoría", FilterType.Category),
            FilterChipConfig("Método de Pago", FilterType.PaymentMethod),
            FilterChipConfig("Etiquetas", FilterType.Tags),
            FilterChipConfig("Periodo", FilterType.Period)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(filterChips) { config ->
                Box {
                    val isPeriodFilterActive = startDate != null || endDate != null

                    FilterChip(
                        selected = when (config.type) {
                            FilterType.Category -> selectedCategories.isNotEmpty()
                            FilterType.PaymentMethod -> selectedPaymentMethods.isNotEmpty()
                            FilterType.Tags -> selectedTags.isNotEmpty()
                            FilterType.Period -> isPeriodFilterActive
                        },
                        onClick = {
                            openFilterMenuType =
                                if (openFilterMenuType == config.type) null else config.type
                        },
                        label = {
                            Text(
                                when (config.type) {
                                    FilterType.Period -> {
                                        if (isPeriodFilterActive) {
                                            val startDisplay =
                                                startDate?.let { formatDateForDisplay(it) }
                                                    ?: "Inicio"
                                            val endDisplay =
                                                endDate?.let { formatDateForDisplay(it) } ?: "Fin"
                                            "Periodo: $startDisplay - $endDisplay"
                                        } else {
                                            "Periodo"
                                        }
                                    }

                                    else -> config.text
                                }
                            )
                        },
                        trailingIcon = {
                            if (config.type == FilterType.Period && isPeriodFilterActive) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Limpiar filtro de periodo",
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clickable {
                                            viewModel.clearDateRangeFilter()
                                            openFilterMenuType = null
                                        }
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Desplegar opciones de filtro"
                                )
                            }
                        }
                    )

                    // --- Dropdown Menu for the current chip ---
                    DropdownMenu(
                        expanded = openFilterMenuType == config.type,
                        onDismissRequest = { openFilterMenuType = null }
                    ) {
                        when (config.type) {
                            FilterType.Category -> {
                                availableCategories.forEach { category ->
                                    val isSelected = selectedCategories.contains(category)
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                category.name.replace("_", " ").lowercase()
                                                    .replaceFirstChar { it.uppercase() }
                                            )
                                        },
                                        onClick = { viewModel.toggleCategoryFilter(category) },
                                        trailingIcon = {
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Seleccionado"
                                                )
                                            }
                                        }
                                    )
                                }
                            }

                            FilterType.PaymentMethod -> {
                                availablePaymentMethods.forEach { method ->
                                    val isSelected = selectedPaymentMethods.contains(method)
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                method.name.replace("_", " ").lowercase()
                                                    .replaceFirstChar { it.uppercase() }
                                            )
                                        },
                                        onClick = { viewModel.togglePaymentMethodFilter(method) },
                                        trailingIcon = {
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Seleccionado"
                                                )
                                            }
                                        }
                                    )
                                }
                            }

                            FilterType.Tags -> {
                                availableTags.forEach { tag ->
                                    val isSelected = selectedTags.contains(tag)
                                    DropdownMenuItem(
                                        text = { Text(tag) },
                                        onClick = { viewModel.toggleTagFilter(tag) },
                                        trailingIcon = {
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Seleccionado"
                                                )
                                            }
                                        }
                                    )
                                }
                            }

                            FilterType.Period -> {
                                // --- Period Filter Menu Content ---
                                DropdownMenuItem(
                                    text = { Text("Desde:") },
                                    onClick = { showStartDatePicker = true },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = "Seleccionar fecha de inicio"
                                        )
                                    }
                                )
                                Text(
                                    text = startDate?.let { formatDateForDisplay(it) }
                                        ?: "Seleccionar...",
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 4.dp
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                DropdownMenuItem(
                                    text = { Text("Hasta:") },
                                    onClick = { showEndDatePicker = true },
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.CalendarMonth,
                                            contentDescription = "Seleccionar fecha de fin"
                                        )
                                    }
                                )
                                Text(
                                    text = endDate?.let { formatDateForDisplay(it) }
                                        ?: "Seleccionar...",
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp,
                                        vertical = 4.dp
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                                DropdownMenuItem(
                                    text = { Text("Limpiar Fechas") },
                                    onClick = {
                                        viewModel.clearDateRangeFilter()
                                        openFilterMenuType = null
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Limpiar"
                                        )
                                    }
                                )
                                // --- End Period Filter Menu Content ---
                            }
                        }
                    }
                }
            }
        }

        if (showStartDatePicker) {
            val initialSelectedDateMillis =
                startDate?.atStartOfDayIn(TimeZone.currentSystemDefault())
                    ?.toEpochMilliseconds()
                    ?: Clock.System.todayIn(TimeZone.currentSystemDefault())
                        .atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = initialSelectedDateMillis
            )
            DatePickerDialog(
                onDismissRequest = { showStartDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val instant = Instant.fromEpochMilliseconds(millis)
                            val selectedLocalDate =
                                instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                            viewModel.setStartDate(selectedLocalDate)
                        }
                        showStartDatePicker = false
                    }) {
                        Text("OK", color = MaterialTheme.colorScheme.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showStartDatePicker = false }) {
                        Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showEndDatePicker) {
            val initialSelectedDateMillis = endDate?.atStartOfDayIn(TimeZone.currentSystemDefault())
                ?.toEpochMilliseconds()
                ?: Clock.System.todayIn(TimeZone.currentSystemDefault())
                    .atStartOfDayIn(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = initialSelectedDateMillis
            )
            DatePickerDialog(
                onDismissRequest = { showEndDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val instant = Instant.fromEpochMilliseconds(millis)
                            val selectedLocalDate =
                                instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
                            viewModel.setEndDate(selectedLocalDate)
                        }
                        showEndDatePicker = false
                    }) {
                        Text("OK", color = MaterialTheme.colorScheme.primary)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEndDatePicker = false }) {
                        Text("Cancelar", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (expensesByDate.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "No hay gastos registrados aún.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp),
            ) {
                val dates = expensesByDate.keys.toList()

                dates.forEachIndexed { index, date ->
                    val expensesForThisDate = expensesByDate.getValue(date)

                    item(key = "date_${date.toEpochDays()}") {
                        Text(
                            text = formatDateForDisplay(date),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 4.dp)
                                .background(MaterialTheme.colorScheme.surface)
                        )
                    }

                    items(expensesForThisDate, key = { it.id }) { expense ->
                        TransactionListItem(transaction = expense.toTransactionForDisplay())
                    }

                    if (index < dates.lastIndex) {
                        item(key = "divider_${date.toEpochDays()}") {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp, horizontal = 0.dp)
                            )
                        }
                    } else {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}