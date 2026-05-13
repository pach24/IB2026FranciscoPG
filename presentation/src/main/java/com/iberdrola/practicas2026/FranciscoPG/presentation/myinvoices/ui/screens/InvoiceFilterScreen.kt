package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.screens
import com.iberdrola.practicas2026.FranciscoPG.presentation.R

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Velocity
import kotlin.math.roundToInt
import androidx.compose.ui.text.font.FontWeight
import com.iberdrola.practicas2026.FranciscoPG.presentation.theme.IberFontBold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.presentation.common.BackTopBar
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.filter.DateRangeSection
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.filter.FilterActionButtons
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoiceFilterUIState
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.filter.PriceRangeSection
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.filter.SafeDatePickerDialog
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.ui.components.filter.StatusFilterSection
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
    onClearFilters: (previousDraft: InvoiceFilters) -> Unit,
    onFilterInteraction: () -> Unit = {},
    onDraftChanged: (InvoiceFilters) -> Unit = {},
    onStartDateTap: () -> Unit = {},
    onEndDateTap: () -> Unit = {},
    onRangeSliderFinished: (Float, Float) -> Unit = { _, _ -> },
    onStatusCheckboxToggled: (InvoiceStatus, Boolean) -> Unit = { _, _ -> }
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

    LaunchedEffect(currentFilters) {
        onDraftChanged(currentFilters)
    }

    val scrollState = rememberScrollState()

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val datePickerStats = uiState.statistics.let { s ->
        val dynOldest = s.dynamicOldestDateMillis.takeIf { it > 0 } ?: s.oldestDateMillis
        val dynNewest = s.dynamicNewestDateMillis.takeIf { it > 0 } ?: s.newestDateMillis
        s.copy(oldestDateMillis = dynOldest, newestDateMillis = dynNewest)
    }

    val actualMinAmount = uiState.statistics.minAmount
    val actualMaxAmount = uiState.statistics.maxAmount.coerceAtLeast(actualMinAmount + 1.0)
    val safeMin = (currentFilters.minAmount ?: actualMinAmount).coerceIn(actualMinAmount, actualMaxAmount)
    val safeMax = (currentFilters.maxAmount ?: actualMaxAmount).coerceIn(safeMin, actualMaxAmount)

    if (showStartDatePicker) {
        SafeDatePickerDialog(
            initialDate = currentFilters.startDate,
            statistics = datePickerStats,
            otherDate = currentFilters.endDate,
            isStartDate = true,
            onDateSelected = { date ->
                currentFilters = currentFilters.copy(startDate = date)
                showStartDatePicker = false
                onFilterInteraction()
            },
            onDismiss = { showStartDatePicker = false }
        )
    }

    if (showEndDatePicker) {
        SafeDatePickerDialog(
            initialDate = currentFilters.endDate,
            statistics = datePickerStats,
            otherDate = currentFilters.startDate,
            isStartDate = false,
            onDateSelected = { date ->
                currentFilters = currentFilters.copy(endDate = date)
                showEndDatePicker = false
                onFilterInteraction()
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
                .drawWithContent {
                    drawContent()
                    val scrolled = scrollState.value
                    val maxScroll = scrollState.maxValue
                    // Alpha proporcional a los primeros/últimos 80px de scroll
                    val topAlpha = (scrolled / 80f).coerceIn(0f, 1f)
                    val bottomAlpha = if (maxScroll > 0) ((maxScroll - scrolled) / 80f).coerceIn(0f, 1f) else 0f
                    if (topAlpha > 0f) {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(colors.background, Color.Transparent),
                                startY = 0f,
                                endY = size.height * 0.15f
                            ),
                            alpha = topAlpha
                        )
                    }
                    if (bottomAlpha > 0f) {
                        drawRect(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, colors.background),
                                startY = size.height * 0.8f,
                                endY = size.height
                            ),
                            alpha = bottomAlpha
                        )
                    }
                }
                .padding(horizontal = Spacing.dp24)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = stringResource(R.string.filter_title),
                fontSize = TextSize.sp20,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = IberFontBold,
                color = colors.darkGreyText,
                modifier = Modifier.padding(top = Spacing.dp16)
            )

            Spacer(modifier = Modifier.height(Spacing.dp16))

            DateRangeSection(
                dateFrom = currentFilters.startDate?.format(DATE_FORMATTER) ?: "",
                dateTo = currentFilters.endDate?.format(DATE_FORMATTER) ?: "",
                onFromClick = {
                    onStartDateTap()
                    showStartDatePicker = true
                },
                onToClick = {
                    onEndDateTap()
                    showEndDatePicker = true
                },
                onFromClear = {
                    currentFilters = currentFilters.copy(startDate = null)
                    onFilterInteraction()
                },
                onToClear = {
                    currentFilters = currentFilters.copy(endDate = null)
                    onFilterInteraction()
                }
            )

            Spacer(modifier = Modifier.height(Spacing.dp18))

            Text(
                text = stringResource(R.string.filter_price_section_title),
                fontSize = TextSize.sp12,
                fontWeight = FontWeight.Bold,
                fontFamily = IberFontBold,
                color = colors.darkGreyText
            )
            Spacer(modifier = Modifier.height(Spacing.dp10))

            PriceRangeSection(
                minPrice = safeMin.toFloat(),
                maxPrice = safeMax.toFloat(),
                minLimit = actualMinAmount.toFloat(),
                maxLimit = actualMaxAmount.toFloat(),
                onRangeChange = { min, max ->
                    val roundedMin = (min * 100.0).roundToInt() / 100.0
                    val roundedMax = (max * 100.0).roundToInt() / 100.0
                    currentFilters = currentFilters.copy(
                        minAmount = if (roundedMin <= actualMinAmount) null else roundedMin,
                        maxAmount = if (roundedMax >= actualMaxAmount) null else roundedMax
                    )
                    onFilterInteraction()
                },
                onRangeChangeFinished = { min, max -> onRangeSliderFinished(min, max) }
            )

            Spacer(modifier = Modifier.height(Spacing.dp32))

            StatusFilterSection(
                statusOptions = statusEntries,
                selectedStatuses = currentFilters.filteredStatuses,
                onStatusToggle = { status ->
                    val wasChecked = status in currentFilters.filteredStatuses
                    val newStates = currentFilters.filteredStatuses.toMutableSet()
                    if (wasChecked) newStates.remove(status) else newStates.add(status)
                    currentFilters = currentFilters.copy(filteredStatuses = newStates)
                    onStatusCheckboxToggled(status, !wasChecked)
                    onFilterInteraction()
                }
            )

            Spacer(modifier = Modifier.height(Spacing.dp32))
        }

        FilterActionButtons(
            onApply = { onApplyFilters(currentFilters) },
            onClear = {
                val previousDraft = currentFilters
                currentFilters = InvoiceFilters()
                onClearFilters(previousDraft)
            }
        )
    }
}

@Preview(name = "Filter - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Filter - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FilterScreenEmptyPreview() {
    IberdrolaTheme {
        Scaffold(
            containerColor = IberdrolaTheme.colors.background,
            topBar = { BackTopBar(onBack = {}) }
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
@Preview(name = "Filter Filled - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FilterScreenFilledPreview() {
    IberdrolaTheme {
        Scaffold(
            containerColor = IberdrolaTheme.colors.background,
            topBar = { BackTopBar(onBack = {}) }
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

@Preview(name = "Filter Scroll Fades - Light", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Filter Scroll Fades - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FilterScreenScrollFadesPreview() {
    IberdrolaTheme {
        Box(modifier = Modifier.heightIn(max = 460.dp)) {
            Scaffold(
                containerColor = IberdrolaTheme.colors.background,
                topBar = { BackTopBar(onBack = {}) }
            ) { padding ->
                FilterContent(
                    modifier = Modifier.padding(padding),
                    uiState = InvoiceFilterUIState(
                        filters = InvoiceFilters(
                            startDate = LocalDate.of(2026, 1, 1),
                            endDate = LocalDate.of(2026, 1, 31),
                            minAmount = 20.0,
                            maxAmount = 150.0,
                            filteredStatuses = setOf(InvoiceStatus.PAID, InvoiceStatus.PENDING)
                        ),
                        statistics = InvoiceFilterUIState.FilterStatistics(maxAmount = 200.0)
                    ),
                    onApplyFilters = {},
                    onClearFilters = { _ -> }
                )
            }
        }
    }
}
