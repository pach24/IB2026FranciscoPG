package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel

import androidx.lifecycle.ViewModel
import com.iberdrola.practicas2026.FranciscoPG.domain.analytics.AnalyticsEvent
import com.iberdrola.practicas2026.FranciscoPG.domain.analytics.AnalyticsTracker
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.newestDate
import com.iberdrola.practicas2026.FranciscoPG.domain.model.oldestDate
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoiceFilterUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

/**
 * ViewModel compartido para el estado de filtros.
 * Fuente única de verdad: los filtros se aplican a todos los tabs simultáneamente.
 */
@HiltViewModel
class FilterViewModel @Inject constructor(
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    // Filtros que el usuario está modificando en la UI (draft)
    private val _filterState = MutableStateFlow(InvoiceFilterUIState())
    val filterState: StateFlow<InvoiceFilterUIState> = _filterState.asStateFlow()

    // Filtros confirmados que realmente se aplican a las listas
    private val _appliedFilters = MutableStateFlow(InvoiceFilters())
    val appliedFilters: StateFlow<InvoiceFilters> = _appliedFilters.asStateFlow()

    // True desde que el usuario pulsa "Aplicar", false tras "Borrar filtros"
    private val _isFilterModeActive = MutableStateFlow(false)
    val isFilterModeActive: StateFlow<Boolean> = _isFilterModeActive.asStateFlow()

    private var _allInvoices: List<Invoice> = emptyList()

    // ── Acciones de filtro ───────────────────────────────────────────────────

    fun updateFilters(filters: InvoiceFilters) {
        _filterState.value = _filterState.value.copy(filters = filters.normalize())
        recomputeDynamicDates()
    }

    fun applyFilters() {
        _appliedFilters.value = _filterState.value.filters
        _isFilterModeActive.value = true
        analyticsTracker.logEvent(
            AnalyticsEvent.APPLY_FILTERS,
            mapOf(AnalyticsEvent.PARAM_FILTER_COUNT to _filterState.value.filters.activeCount.toString())
        )
    }

    fun restoreFilters(draft: InvoiceFilters, applied: InvoiceFilters) {
        _filterState.value = _filterState.value.copy(filters = draft)
        _appliedFilters.value = applied
        _isFilterModeActive.value = applied != InvoiceFilters()
        recomputeDynamicDates()
    }

    fun clearFilters() {
        _filterState.value = _filterState.value.copy(filters = InvoiceFilters())
        _appliedFilters.value = InvoiceFilters()
        _isFilterModeActive.value = false
        recomputeDynamicDates()
        analyticsTracker.logEvent(AnalyticsEvent.CLEAR_FILTERS)
    }

    fun onStartDateTap() = analyticsTracker.logEvent(AnalyticsEvent.FILTER_TAP_START_DATE)

    fun onEndDateTap() = analyticsTracker.logEvent(AnalyticsEvent.FILTER_TAP_END_DATE)

    fun onRangeSliderFinished(min: Float, max: Float) = analyticsTracker.logEvent(
        AnalyticsEvent.FILTER_RANGE_SLIDER_CHANGED,
        mapOf(
            AnalyticsEvent.PARAM_MIN_AMOUNT to min.toInt().toString(),
            AnalyticsEvent.PARAM_MAX_AMOUNT to max.toInt().toString()
        )
    )

    fun onStatusCheckboxToggled(status: InvoiceStatus, checked: Boolean) = analyticsTracker.logEvent(
        AnalyticsEvent.FILTER_TAP_STATUS_CHECKBOX,
        mapOf(
            AnalyticsEvent.PARAM_STATUS to status.name,
            AnalyticsEvent.PARAM_CHECKED to checked.toString()
        )
    )

    // ── Estadísticas ─────────────────────────────────────────────────────────

    /**
     * Actualiza las estadísticas del filtro combinando datos de ambos tabs.
     * Se llama desde InvoicesRoute cuando las facturas de cualquier tab cambian.
     */
    fun updateStatistics(
        allInvoices: List<Invoice>,
        minAmount: Double,
        maxAmount: Double,
        oldestDate: LocalDate?,
        newestDate: LocalDate?
    ) {
        _allInvoices = allInvoices
        val newStats = InvoiceFilterUIState.FilterStatistics(
            minAmount = minAmount,
            maxAmount = maxAmount,
            oldestDateMillis = oldestDate.toEpochMilli(),
            newestDateMillis = newestDate.toEpochMilli()
        )

        val currentUI = _filterState.value
        val oldFilters = currentUI.filters
        val oldStats = currentUI.statistics

        // Ajustar slider (draft) al nuevo rango solo si el usuario lo había tocado
        val finalMin = if (oldFilters.minAmount != null) {
            if (oldFilters.minAmount!! <= oldStats.minAmount * 1.01) minAmount
            else oldFilters.minAmount!!.coerceIn(minAmount, maxAmount)
        } else null
        val finalMax = if (oldFilters.maxAmount != null) {
            if (oldFilters.maxAmount!! >= oldStats.maxAmount * 0.99) maxAmount
            else oldFilters.maxAmount!!.coerceIn(finalMin ?: minAmount, maxAmount)
        } else null

        _filterState.value = currentUI.copy(
            filters = oldFilters.copy(minAmount = finalMin, maxAmount = finalMax),
            statistics = newStats
        )

        // Ajustar filtros aplicados al nuevo rango de datos
        expandAppliedFilters(oldStats, newStats, minAmount, maxAmount, oldestDate, newestDate)
        recomputeDynamicDates()
    }

    /**
     * Expande los filtros aplicados cuando los datos cambian (ej: pull-to-refresh).
     * Si el usuario tenía un límite al extremo del rango anterior (intención = "sin límite"),
     * se expande al nuevo extremo. Si tenía un valor personalizado, se respeta.
     */
    private fun expandAppliedFilters(
        oldStats: InvoiceFilterUIState.FilterStatistics,
        newStats: InvoiceFilterUIState.FilterStatistics,
        minAmount: Double,
        maxAmount: Double,
        oldestDate: LocalDate?,
        newestDate: LocalDate?
    ) {
        val applied = _appliedFilters.value
        if (applied == InvoiceFilters()) return // sin filtros activos, nada que ajustar

        val expandMin = applied.minAmount != null && applied.minAmount!! <= oldStats.minAmount * 1.01
        val expandMax = applied.maxAmount != null && applied.maxAmount!! >= oldStats.maxAmount * 0.99
        val expandStartDate = applied.startDate != null
                && oldStats.oldestDateMillis > 0
                && applied.startDate!!.toEpochMilli() <= oldStats.oldestDateMillis
        val expandEndDate = applied.endDate != null
                && oldStats.newestDateMillis > 0
                && applied.endDate!!.toEpochMilli() >= oldStats.newestDateMillis

        _appliedFilters.value = applied.copy(
            minAmount = if (expandMin) minAmount else applied.minAmount,
            maxAmount = if (expandMax) maxAmount else applied.maxAmount,
            startDate = if (expandStartDate) oldestDate else applied.startDate,
            endDate = if (expandEndDate) newestDate else applied.endDate
        )
    }

    private fun recomputeDynamicDates() {
        val draftFilters = _filterState.value.filters
        val draftMin = draftFilters.minAmount
        val draftMax = draftFilters.maxAmount

        val filtered = if (draftMin != null || draftMax != null) {
            _allInvoices.filter { invoice ->
                (draftMin == null || invoice.amount >= draftMin) &&
                (draftMax == null || invoice.amount <= draftMax)
            }
        } else {
            _allInvoices
        }

        val dynOldest = filtered.oldestDate()
        val dynNewest = filtered.newestDate()

        _filterState.value = _filterState.value.copy(
            statistics = _filterState.value.statistics.copy(
                dynamicOldestDateMillis = dynOldest.toEpochMilli(),
                dynamicNewestDateMillis = dynNewest.toEpochMilli()
            )
        )
    }

    private fun LocalDate?.toEpochMilli(): Long =
        this?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli() ?: 0L
}
