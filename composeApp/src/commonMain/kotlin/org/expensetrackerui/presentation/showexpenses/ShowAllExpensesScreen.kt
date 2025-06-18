package org.expensetrackerui.presentation.showexpenses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.runtime.derivedStateOf
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.expensetrackerui.presentation.TransactionListItem
import org.expensetrackerui.util.formatDateForDisplay
import androidx.compose.runtime.LaunchedEffect // Import LaunchedEffect

sealed class FilterType {
    object Category : FilterType()
    object PaymentMethod : FilterType()
    object Tags : FilterType()
    object Period : FilterType()
}

data class FilterChipConfig(val text: String, val type: FilterType)

@OptIn(ExperimentalMaterial3Api::class)
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

    // Collected *staged* filter states from ViewModel (for UI display)
    val stagedSelectedCategories by viewModel.stagedSelectedCategories.collectAsState()
    val stagedSelectedPaymentMethods by viewModel.stagedSelectedPaymentMethods.collectAsState()
    val stagedSelectedTags by viewModel.stagedSelectedTags.collectAsState()
    val stagedStartDate by viewModel.stagedStartDate.collectAsState()
    val stagedEndDate by viewModel.stagedEndDate.collectAsState()

    // Available filter options from ViewModel
    val availableCategories by viewModel.availableCategories.collectAsState()
    val availablePaymentMethods by viewModel.availablePaymentMethods.collectAsState()
    val availableTags by viewModel.availableTags.collectAsState()


    // --- Date Picker Dialog states ---
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    // --- LazyColumn State for Scrolling ---
    val lazyColumnState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    // --- State to control "Scroll to Top" button visibility ---
    val showScrollToTopButton by remember {
        // Derived state to observe if the first item is not visible (i.e., scrolled down)
        // This will recompose when firstVisibleItemIndex changes.
        // We set a threshold (e.g., 5 items) before showing the button to avoid flickering
        // when scrolling just a little.
        derivedStateOf {
            lazyColumnState.firstVisibleItemIndex > 0 // Show if not at the very top
        }
    }


    // --- LaunchedEffect for reliable scroll to top after filtering/clearing ---
    LaunchedEffect(expensesByDate) {
        if (expensesByDate.isNotEmpty()) {
            delay(50) // Small delay to allow recomposition and layout calculation
            lazyColumnState.scrollToItem(0)
        }
    }

    // The main layout is now a Box to allow floating elements
    Box(modifier = modifier.fillMaxSize()) { // Added a root Box
        Column(
            modifier = Modifier
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

            // --- Scrollable Row of Filter Chips, "Buscar" Button, and "Limpiar Filtros" Button ---
            val filterChips = listOf(
                FilterChipConfig("Categoría", FilterType.Category),
                FilterChipConfig("Método de Pago", FilterType.PaymentMethod),
                FilterChipConfig("Etiquetas", FilterType.Tags),
                FilterChipConfig("Periodo", FilterType.Period)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()), // Allows horizontal scrolling if content overflows
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically // Align items vertically
            ) {

                // Condition to enable "Limpiar Filtros" button
                val anyStagedFilterPresent =
                    stagedSelectedCategories.isNotEmpty() ||
                            stagedSelectedPaymentMethods.isNotEmpty() ||
                            stagedSelectedTags.isNotEmpty() ||
                            stagedStartDate != null ||
                            stagedEndDate != null

                // --- "Buscar" Button, always enabled, applies staged filters ---
                Surface(
                    modifier = Modifier
                        .padding(start = 0.dp) // No starting padding for the first element
                        .height(32.dp)
                        .wrapContentWidth()
                        .clickable(
                            onClick = {
                                viewModel.applyFilters() // Apply whatever is staged (even if nothing is staged)
                                openFilterMenuType = null
                                // The scroll is now handled by the LaunchedEffect
                            }
                        ),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF2563EB), // Always blue
                    contentColor = Color.White // Always white text
                ) {
                    Box( // Use Box for centering content inside Surface
                        modifier = Modifier
                            .padding(horizontal = 8.dp), // Adjusted padding for icon
                        contentAlignment = Alignment.Center // Center the icon
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = LocalContentColor.current,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .padding(start = 8.dp) // Add space between "Buscar" and "Limpiar"
                        .height(32.dp)
                        .wrapContentWidth()
                        .clickable(
                            enabled = anyStagedFilterPresent, // Enabled if there are staged filters to clear
                            onClick = {
                                viewModel.resetStagedFilters() // ONLY reset staged filters
                                openFilterMenuType = null // Close any open filter menu
                            }
                        ),
                    shape = RoundedCornerShape(16.dp),
                    color = if (anyStagedFilterPresent) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceVariant, // Red when enabled, default when disabled
                    contentColor = if (anyStagedFilterPresent) MaterialTheme.colorScheme.onError else MaterialTheme.colorScheme.onSurfaceVariant // Text color based on enabled state
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 8.dp), // Adjusted padding for icon
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Limpiar Filtros",
                            tint = LocalContentColor.current,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                filterChips.forEach { config ->
                    Box {
                        val isPeriodFilterStaged = stagedStartDate != null || stagedEndDate != null

                        FilterChip(
                            selected = when (config.type) {
                                FilterType.Category -> stagedSelectedCategories.isNotEmpty()
                                FilterType.PaymentMethod -> stagedSelectedPaymentMethods.isNotEmpty()
                                FilterType.Tags -> stagedSelectedTags.isNotEmpty()
                                FilterType.Period -> isPeriodFilterStaged
                            },
                            onClick = {
                                openFilterMenuType =
                                    if (openFilterMenuType == config.type) null else config.type
                            },
                            label = {
                                Text(
                                    when (config.type) {
                                        FilterType.Period -> {
                                            if (isPeriodFilterStaged) {
                                                val startDisplay =
                                                    stagedStartDate?.let { formatDateForDisplay(it) }
                                                        ?: "Inicio"
                                                val endDisplay =
                                                    stagedEndDate?.let { formatDateForDisplay(it) }
                                                        ?: "Fin"
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
                                if (config.type == FilterType.Period && isPeriodFilterStaged) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Limpiar filtro de periodo",
                                        modifier = Modifier
                                            .size(16.dp)
                                            .clickable {
                                                viewModel.clearStagedDateRangeFilter() // Clear staged date filter
                                                // You might want to keep the menu open if the user is making multiple changes
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

                        DropdownMenu(
                            expanded = openFilterMenuType == config.type,
                            onDismissRequest = { openFilterMenuType = null }
                        ) {
                            when (config.type) {
                                FilterType.Category -> {
                                    availableCategories.forEach { category ->
                                        val isSelected =
                                            stagedSelectedCategories.contains(category) // Use staged
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    category.name.replace("_", " ").lowercase()
                                                        .replaceFirstChar { it.uppercase() }
                                                )
                                            },
                                            onClick = { viewModel.toggleCategoryFilter(category) }, // Update staged
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
                                        val isSelected =
                                            stagedSelectedPaymentMethods.contains(method) // Use staged
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    method.name.replace("_", " ").lowercase()
                                                        .replaceFirstChar { it.uppercase() }
                                                )
                                            },
                                            onClick = { viewModel.togglePaymentMethodFilter(method) }, // Update staged
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
                                        val isSelected =
                                            stagedSelectedTags.contains(tag) // Use staged
                                        DropdownMenuItem(
                                            text = { Text(tag) },
                                            onClick = { viewModel.toggleTagFilter(tag) }, // Update staged
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
                                    // ... (Your existing Period filter menu content)
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
                                        text = stagedStartDate?.let { formatDateForDisplay(it) } // Use staged
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
                                        text = stagedEndDate?.let { formatDateForDisplay(it) } // Use staged
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
                                            viewModel.clearStagedDateRangeFilter()
                                        },
                                        leadingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Limpiar"
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (showStartDatePicker) {
                val initialSelectedDateMillis =
                    stagedStartDate?.atStartOfDayIn(TimeZone.currentSystemDefault())
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
                                viewModel.setStagedStartDate(selectedLocalDate) // Update staged start date
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
                val initialSelectedDateMillis =
                    stagedEndDate?.atStartOfDayIn(TimeZone.currentSystemDefault())
                        ?.toEpochMilliseconds() // Use staged
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
                                viewModel.setStagedEndDate(selectedLocalDate) // Update staged end date
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
                    state = lazyColumnState
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

        // --- Scroll to Top Button (Floating) ---
        AnimatedVisibility( // Add animation for appearance/disappearance
            visible = showScrollToTopButton,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomEnd) // Position at bottom end of the parent Box
                .padding(24.dp) // Padding from the edges
        ) {
            Surface(
                modifier = Modifier
                    .size(56.dp) // Standard FAB size
                    .clickable {
                        coroutineScope.launch {
                            lazyColumnState.animateScrollToItem(0) // Smooth scroll to top
                        }
                    },
                shape = CircleShape, // Make it a circular button
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shadowElevation = 6.dp // Add a subtle shadow for elevation
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward, // Use the new upward arrow icon
                        contentDescription = "Scroll to top",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}