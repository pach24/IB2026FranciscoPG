package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel

import androidx.lifecycle.ViewModel
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.components.filter.InvoiceFilterUIState
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
class FilterViewModel @Inject constructor() : ViewModel() {

    // Filtros que el usuario está modificando en la UI (draft)
    private val _filterState = MutableStateFlow(InvoiceFilterUIState())
    val filterState: StateFlow<InvoiceFilterUIState> = _filterState.asStateFlow()

    // Filtros confirmados que realmente se aplican a las listas
    private val _appliedFilters = MutableStateFlow(InvoiceFilters())
    val appliedFilters: StateFlow<InvoiceFilters> = _appliedFilters.asStateFlow()

    // ── Acciones de filtro ───────────────────────────────────────────────────

    fun updateFilters(filters: InvoiceFilters) {
        _filterState.value = _filterState.value.copy(filters = filters.normalize())
    }

    fun applyFilters() {
        _appliedFilters.value = _filterState.value.filters
    }

    fun restoreFilters(filters: InvoiceFilters) {
        _filterState.value = _filterState.value.copy(filters = filters)
        _appliedFilters.value = filters
    }

    fun clearFilters() {
        val maxAmount = _filterState.value.statistics.maxAmount
        val cleanFilters = InvoiceFilters(
            minAmount = 0.0,
            maxAmount = maxAmount,
            startDate = null,
            endDate = null,
            filteredStatuses = emptySet()
        )
        _filterState.value = _filterState.value.copy(filters = cleanFilters)
        _appliedFilters.value = InvoiceFilters()
    }

    // ── Estadísticas ─────────────────────────────────────────────────────────

    /**
     * Actualiza las estadísticas del filtro combinando datos de ambos tabs.
     * Se llama desde InvoicesRoute cuando las facturas de cualquier tab cambian.
     */
    fun updateStatistics(
        maxAmount: Double,
        oldestDate: LocalDate?,
        newestDate: LocalDate?
    ) {
        val newStats = InvoiceFilterUIState.FilterStatistics(
            maxAmount = maxAmount,
            oldestDateMillis = oldestDate.toEpochMilli(),
            newestDateMillis = newestDate.toEpochMilli()
        )

        val currentUI = _filterState.value
        val oldFilters = currentUI.filters
        val oldStats = currentUI.statistics

        // Ajustar slider (draft) al nuevo rango
        val finalMin = (oldFilters.minAmount ?: 0.0).coerceIn(0.0, maxAmount)
        val finalMax = if (oldFilters.maxAmount != null && oldFilters.maxAmount!! >= oldStats.maxAmount * 0.99) {
            maxAmount
        } else {
            (oldFilters.maxAmount ?: maxAmount).coerceIn(finalMin, maxAmount)
        }

        _filterState.value = currentUI.copy(
            filters = oldFilters.copy(minAmount = finalMin, maxAmount = finalMax),
            statistics = newStats
        )

        // Ajustar filtros aplicados al nuevo rango de datos
        expandAppliedFilters(oldStats, newStats, maxAmount, oldestDate, newestDate)
    }

    /**
     * Expande los filtros aplicados cuando los datos cambian (ej: pull-to-refresh).
     * Si el usuario tenía un límite al extremo del rango anterior (intención = "sin límite"),
     * se expande al nuevo extremo. Si tenía un valor personalizado, se respeta.
     */
    private fun expandAppliedFilters(
        oldStats: InvoiceFilterUIState.FilterStatistics,
        newStats: InvoiceFilterUIState.FilterStatistics,
        maxAmount: Double,
        oldestDate: LocalDate?,
        newestDate: LocalDate?
    ) {
        val applied = _appliedFilters.value
        if (applied == InvoiceFilters()) return // sin filtros activos, nada que ajustar

        val expandMax = applied.maxAmount != null && applied.maxAmount!! >= oldStats.maxAmount * 0.99
        val expandStartDate = applied.startDate != null
                && oldStats.oldestDateMillis > 0
                && applied.startDate!!.toEpochMilli() <= oldStats.oldestDateMillis
        val expandEndDate = applied.endDate != null
                && oldStats.newestDateMillis > 0
                && applied.endDate!!.toEpochMilli() >= oldStats.newestDateMillis

        _appliedFilters.value = applied.copy(
            maxAmount = if (expandMax) maxAmount else applied.maxAmount,
            startDate = if (expandStartDate) oldestDate else applied.startDate,
            endDate = if (expandEndDate) newestDate else applied.endDate
        )
    }

    private fun LocalDate?.toEpochMilli(): Long =
        this?.atStartOfDay(ZoneOffset.UTC)?.toInstant()?.toEpochMilli() ?: 0L
}
