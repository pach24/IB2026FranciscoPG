package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.FranciscoPG.R
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter.DateRangeSection
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter.FilterActionButtons
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter.InvoiceFilterUIState
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter.PriceRangeSection
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter.StatusFilterSection
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberdrolaTheme
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.Spacing
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.TextSize
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterContent(
    modifier: Modifier = Modifier,
    uiState: InvoiceFilterUIState,
    onApplyFilters: (InvoiceFilters) -> Unit,
    onClearFilters: (previousDraft: InvoiceFilters) -> Unit
) {
    val statusEntries = listOf(
        InvoiceStatus.PAID to stringResource(R.string.filter_status_paid),
        InvoiceStatus.PENDING to stringResource(R.string.filter_status_pending),
        InvoiceStatus.PROCESSING to stringResource(R.string.filter_status_processing),
        InvoiceStatus.CANCELLED to stringResource(R.string.filter_status_cancelled),
        InvoiceStatus.FIXED_FEE to stringResource(R.string.filter_status_fixed_fee)
    )
    val colors = IberdrolaTheme.colors

    var currentFilters by remember(uiState.filters) { mutableStateOf(uiState.filters) }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val actualMaxAmount = uiState.statistics.maxAmount.coerceAtLeast(1.0)
    val safeMin = (currentFilters.minAmount ?: 0.0).coerceIn(0.0, actualMaxAmount)
    val safeMax = (currentFilters.maxAmount ?: actualMaxAmount).coerceIn(safeMin, actualMaxAmount)

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
            Text(
                text = stringResource(R.string.filter_title),
                fontSize = TextSize.sp20,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = IberFontBold,
                color = colors.darkGreyText,
                modifier = Modifier.padding(top = Spacing.dp16)
            )

            Spacer(modifier = Modifier.height(Spacing.dp24))

            DateRangeSection(
                dateFrom = currentFilters.startDate?.format(DATE_FORMATTER) ?: "",
                dateTo = currentFilters.endDate?.format(DATE_FORMATTER) ?: "",
                onFromClick = { showStartDatePicker = true },
                onToClick = { showEndDatePicker = true },
                onFromClear = { currentFilters = currentFilters.copy(startDate = null) },
                onToClear = { currentFilters = currentFilters.copy(endDate = null) }
            )

            Spacer(modifier = Modifier.height(Spacing.dp32))

            Text(
                text = stringResource(R.string.filter_price_section_title),
                fontSize = TextSize.sp12,
                fontWeight = FontWeight.Bold,
                fontFamily = IberFontBold,
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

            StatusFilterSection(
                statusOptions = statusEntries,
                selectedStatuses = currentFilters.filteredStatuses,
                onStatusToggle = { status ->
                    val newStates = currentFilters.filteredStatuses.toMutableSet()
                    if (status in newStates) newStates.remove(status) else newStates.add(status)
                    currentFilters = currentFilters.copy(filteredStatuses = newStates)
                }
            )

            Spacer(modifier = Modifier.height(Spacing.dp32))

            FilterActionButtons(
                onApply = { onApplyFilters(currentFilters) },
                onClear = {
                    val previousDraft = currentFilters
                    currentFilters = InvoiceFilters(
                        minAmount = 0.0,
                        maxAmount = actualMaxAmount,
                        startDate = null,
                        endDate = null,
                        filteredStatuses = emptySet()
                    )
                    onClearFilters(previousDraft)
                }
            )
        }
    }
}

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
                        filteredStatuses = setOf(InvoiceStatus.PAID)
                    ),
                    statistics = InvoiceFilterUIState.FilterStatistics(maxAmount = 500.0)
                ),
                onApplyFilters = {},
                onClearFilters = { _ -> }
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
                        filteredStatuses = setOf(InvoiceStatus.PAID)
                    ),
                    statistics = InvoiceFilterUIState.FilterStatistics(maxAmount = 200.0)
                ),
                onApplyFilters = {},
                onClearFilters = { _ -> }
            )
        }
    }
}
