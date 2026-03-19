package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter.DateRangeSection
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter.FilterActionButtons
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter.InvoiceFilterUIState
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter.PriceRangeSection
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter.StatusFilterSection
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.MyInvoicesViewModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private val FilterBold = FontFamily(Font(R.font.iberpangea_bold, FontWeight.Bold))
private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

// Barra superior de la pantalla de filtros con botón de retroceso
@Composable
fun FilterTopBar(onBack: () -> Unit) {
    val colors = IberdrolaTheme.colors
    Surface(
        color = colors.background,
        shadowElevation = Spacing.dp0
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp16)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = Spacing.dp24)
                    .clickable { onBack() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = colors.iberdrolaDarkGreen,
                    modifier = Modifier.size(Spacing.dp24)
                )
                Text(
                    text = stringResource(R.string.filter_back),
                    color = colors.iberdrolaDarkGreen,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FilterBold,
                    textDecoration = TextDecoration.Underline,
                    fontSize = TextSize.sp16
                )
            }
        }
    }
}

// Contenido principal del filtro: fecha, importe, estado y botones de acción
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterContent(
    modifier: Modifier = Modifier,
    uiState: InvoiceFilterUIState,
    onApplyFilters: (InvoiceFilters) -> Unit,
    onClearFilters: () -> Unit,
    statusOptions: List<String> = listOf("Pagadas", "Pendientes de Pago", "En trámite de cobro", "Anuladas", "Cuota Fija")
) {
    val colors = IberdrolaTheme.colors

    // Estado local del filtro (draft) — solo se envía al ViewModel al pulsar "Aplicar"
    var currentFilters by remember(uiState.filters) { mutableStateOf(uiState.filters) }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val actualMaxAmount = uiState.statistics.maxAmount.coerceAtLeast(1.0)
    val safeMin = (currentFilters.minAmount ?: 0.0).coerceIn(0.0, actualMaxAmount)
    val safeMax = (currentFilters.maxAmount ?: actualMaxAmount).coerceIn(safeMin, actualMaxAmount)

    // Diálogos de fecha
    if (showStartDatePicker) {
        SafeDatePickerDialog(
            initialDate = currentFilters.startDate,
            statistics = uiState.statistics,
            otherDate = currentFilters.endDate,
            isStartDate = true,
            onDateSelected = { date ->
                currentFilters = currentFilters.copy(startDate = date)
                showStartDatePicker = false
            },
            onDismiss = { showStartDatePicker = false }
        )
    }

    if (showEndDatePicker) {
        SafeDatePickerDialog(
            initialDate = currentFilters.endDate,
            statistics = uiState.statistics,
            otherDate = currentFilters.startDate,
            isStartDate = false,
            onDateSelected = { date ->
                currentFilters = currentFilters.copy(endDate = date)
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = Spacing.dp24)
                .verticalScroll(rememberScrollState())
        ) {
            // Título
            Text(
                text = stringResource(R.string.filter_title),
                fontSize = TextSize.sp22,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FilterBold,
                color = colors.darkGreyText,
                modifier = Modifier.padding(top = Spacing.dp16)
            )

            Spacer(modifier = Modifier.height(Spacing.dp32))

            // Sección fecha
            DateRangeSection(
                dateFrom = currentFilters.startDate?.format(DATE_FORMATTER) ?: "",
                dateTo = currentFilters.endDate?.format(DATE_FORMATTER) ?: "",
                onFromClick = { showStartDatePicker = true },
                onToClick = { showEndDatePicker = true }
            )

            Spacer(modifier = Modifier.height(Spacing.dp32))

            // Sección importe
            Text(
                text = stringResource(R.string.filter_price_section_title),
                fontSize = TextSize.sp14,
                fontWeight = FontWeight.Bold,
                fontFamily = FilterBold,
                color = colors.darkGreyText
            )
            Spacer(modifier = Modifier.height(Spacing.dp16))

            PriceRangeSection(
                minPrice = safeMin.toFloat(),
                maxPrice = safeMax.toFloat(),
                minLimit = 0f,
                maxLimit = actualMaxAmount.toFloat(),
                onRangeChange = { min, max ->
                    currentFilters = currentFilters.copy(
                        minAmount = min.toDouble(),
                        maxAmount = max.toDouble()
                    )
                }
            )

            Spacer(modifier = Modifier.height(Spacing.dp32))

            // Sección estado
            StatusFilterSection(
                statusOptions = statusOptions,
                selectedStatuses = currentFilters.filteredStatuses,
                onStatusToggle = { status ->
                    val newStates = currentFilters.filteredStatuses.toMutableSet()
                    if (status in newStates) newStates.remove(status) else newStates.add(status)
                    currentFilters = currentFilters.copy(filteredStatuses = newStates)
                }
            )

            Spacer(modifier = Modifier.height(Spacing.dp48))

            // Botones
            FilterActionButtons(
                onApply = { onApplyFilters(currentFilters) },
                onClear = {
                    currentFilters = InvoiceFilters(
                        minAmount = 0.0,
                        maxAmount = actualMaxAmount,
                        startDate = null,
                        endDate = null,
                        filteredStatuses = emptySet()
                    )
                    onClearFilters()
                }
            )
        }
    }
}

// ══════════════════════════════════════════
// SAFE DATE PICKER DIALOG
// ══════════════════════════════════════════

// Diálogo de selección de fecha con validación de límites según datos de facturas
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafeDatePickerDialog(
    initialDate: LocalDate?,
    statistics: InvoiceFilterUIState.FilterStatistics,
    otherDate: LocalDate?,
    isStartDate: Boolean,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val colors = IberdrolaTheme.colors

    val minDateAllowed = statistics.oldestDateMillis
    val maxDateAllowed = statistics.newestDateMillis

    // Validación cruzada: startDate <= endDate
    val validationMin = if (!isStartDate && otherDate != null) {
        otherDate.toEpochMilli().coerceAtLeast(minDateAllowed)
    } else minDateAllowed

    val validationMax = if (isStartDate && otherDate != null) {
        otherDate.toEpochMilli().coerceAtMost(maxDateAllowed)
    } else maxDateAllowed

    val rawSelection = initialDate?.toEpochMilli()
    val safeSelection = if (rawSelection != null && validationMin > 0 && rawSelection in validationMin..validationMax) {
        rawSelection
    } else {
        if (isStartDate && minDateAllowed > 0) minDateAllowed
        else if (!isStartDate && maxDateAllowed > 0) maxDateAllowed
        else null
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = safeSelection,
        selectableDates = if (minDateAllowed > 0 && maxDateAllowed > 0) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis in validationMin..validationMax
                }

                override fun isSelectableYear(year: Int): Boolean {
                    val yearStartMillis = LocalDate.of(year, 1, 1).toEpochMilli()
                    val yearEndMillis = LocalDate.of(year, 12, 31).toEpochMilli()
                    return !(yearEndMillis < validationMin || yearStartMillis > validationMax)
                }
            }
        } else {
            object : SelectableDates {}
        }
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    if (minDateAllowed <= 0 || maxDateAllowed <= 0 || millis in validationMin..validationMax) {
                        val date = Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate()
                        onDateSelected(date)
                    }
                }
                onDismiss()
            }) {
                Text("OK", color = colors.iberdrolaDarkGreen, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = colors.iberdrolaDarkGreen)
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                todayContentColor = colors.iberdrolaDarkGreen,
                todayDateBorderColor = colors.iberdrolaDarkGreen,
                selectedDayContainerColor = colors.iberdrolaDarkGreen,
                selectedDayContentColor = Color.White
            )
        )
    }
}

private fun LocalDate.toEpochMilli(): Long =
    atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

// ══════════════════════════════════════════
// ROUTE
// ══════════════════════════════════════════

// Ruta principal que conecta el ViewModel con la pantalla de filtros
@Composable
fun FilterRoute(
    tab: Int,
    onBack: () -> Unit,
    viewModel: MyInvoicesViewModel
) {
    val filterUIState by viewModel.filterState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = IberdrolaTheme.colors.background,
        modifier = Modifier.statusBarsPadding(),
        topBar = { FilterTopBar(onBack = onBack) }
    ) { padding ->
        FilterContent(
            modifier = Modifier.padding(padding),
            uiState = filterUIState,
            onApplyFilters = { filters ->
                viewModel.updateFilters(filters)
                viewModel.applyFilters()
                onBack()
            },
            onClearFilters = {
                viewModel.clearFilters()
                onBack()
            }
        )
    }
}

// ══════════════════════════════════════════
// PREVIEWS
// ══════════════════════════════════════════

@Preview(name = "Filter - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Filter - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FilterScreenEmptyPreview() {
    IberdrolaTheme {
        Scaffold(
            containerColor = IberdrolaTheme.colors.background,
            topBar = { FilterTopBar(onBack = {}) }
        ) { padding ->
            FilterContent(
                modifier = Modifier.padding(padding),
                uiState = InvoiceFilterUIState(
                    filters = InvoiceFilters(
                        filteredStatuses = setOf("Pagadas")
                    ),
                    statistics = InvoiceFilterUIState.FilterStatistics(maxAmount = 500.0)
                ),
                onApplyFilters = {},
                onClearFilters = {}
            )
        }
    }
}

@Preview(name = "Filter Filled - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Filter Filled - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FilterScreenFilledPreview() {
    IberdrolaTheme {
        Scaffold(
            containerColor = IberdrolaTheme.colors.background,
            topBar = { FilterTopBar(onBack = {}) }
        ) { padding ->
            FilterContent(
                modifier = Modifier.padding(padding),
                uiState = InvoiceFilterUIState(
                    filters = InvoiceFilters(
                        startDate = LocalDate.of(2026, 1, 1),
                        endDate = LocalDate.of(2026, 1, 31),
                        minAmount = 20.0,
                        maxAmount = 150.0,
                        filteredStatuses = setOf("Pagadas")
                    ),
                    statistics = InvoiceFilterUIState.FilterStatistics(maxAmount = 200.0)
                ),
                onApplyFilters = {},
                onClearFilters = {}
            )
        }
    }
}

@Preview(name = "TopBar - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "TopBar - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FilterTopBarPreview() {
    IberdrolaTheme {
        FilterTopBar(onBack = {})
    }
}
