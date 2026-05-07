package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoiceFilterUIState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class FilterViewModelTest {

    private lateinit var viewModel: FilterViewModel

    @Before
    fun setUp() {
        viewModel = FilterViewModel()
    }

    // ── Estado inicial ────────────────────────────────────────────────────────

    @Test
    fun `initial state has empty filters`() {
        assertEquals(InvoiceFilters(), viewModel.filterState.value.filters)
        assertEquals(InvoiceFilters(), viewModel.appliedFilters.value)
        assertFalse(viewModel.isFilterModeActive.value)
    }

    @Test
    fun `initial statistics are zero`() {
        val stats = viewModel.filterState.value.statistics
        assertEquals(0.0, stats.minAmount, 0.0)
        assertEquals(0.0, stats.maxAmount, 0.0)
        assertEquals(0L, stats.oldestDateMillis)
        assertEquals(0L, stats.newestDateMillis)
    }

    // ── updateFilters ─────────────────────────────────────────────────────────

    @Test
    fun `updateFilters changes draft but not applied`() {
        val draft = InvoiceFilters(minAmount = 10.0, maxAmount = 100.0)

        viewModel.updateFilters(draft)

        assertEquals(draft, viewModel.filterState.value.filters)
        assertEquals(InvoiceFilters(), viewModel.appliedFilters.value)
    }

    @Test
    fun `updateFilters normalizes inverted amounts`() {
        val inverted = InvoiceFilters(minAmount = 200.0, maxAmount = 50.0)

        viewModel.updateFilters(inverted)

        val result = viewModel.filterState.value.filters
        assertEquals(50.0, result.minAmount!!, 0.0)
        assertEquals(200.0, result.maxAmount!!, 0.0)
    }

    @Test
    fun `updateFilters normalizes inverted dates`() {
        val start = LocalDate.of(2026, 6, 1)
        val end = LocalDate.of(2026, 1, 1)
        val inverted = InvoiceFilters(startDate = start, endDate = end)

        viewModel.updateFilters(inverted)

        val result = viewModel.filterState.value.filters
        assertEquals(end, result.startDate)
        assertEquals(start, result.endDate)
    }

    // ── applyFilters ──────────────────────────────────────────────────────────

    @Test
    fun `applyFilters copies draft to applied and activates filter mode`() {
        val draft = InvoiceFilters(
            filteredStatuses = setOf(InvoiceStatus.PAID)
        )
        viewModel.updateFilters(draft)

        viewModel.applyFilters()

        assertEquals(draft, viewModel.appliedFilters.value)
        assertTrue(viewModel.isFilterModeActive.value)
    }

    @Test
    fun `applyFilters with empty filters still activates filter mode`() {
        viewModel.applyFilters()

        assertTrue(viewModel.isFilterModeActive.value)
    }

    // ── clearFilters ──────────────────────────────────────────────────────────

    @Test
    fun `clearFilters resets draft, applied, and deactivates filter mode`() {
        viewModel.updateFilters(InvoiceFilters(minAmount = 50.0))
        viewModel.applyFilters()

        viewModel.clearFilters()

        assertEquals(InvoiceFilters(), viewModel.filterState.value.filters)
        assertEquals(InvoiceFilters(), viewModel.appliedFilters.value)
        assertFalse(viewModel.isFilterModeActive.value)
    }

    // ── restoreFilters ────────────────────────────────────────────────────────

    @Test
    fun `restoreFilters restores draft and applied from snapshot`() {
        val draft = InvoiceFilters(minAmount = 10.0, maxAmount = 80.0)
        val applied = InvoiceFilters(
            filteredStatuses = setOf(InvoiceStatus.PENDING)
        )

        viewModel.restoreFilters(draft, applied)

        assertEquals(draft, viewModel.filterState.value.filters)
        assertEquals(applied, viewModel.appliedFilters.value)
        assertTrue(viewModel.isFilterModeActive.value)
    }

    @Test
    fun `restoreFilters with empty applied deactivates filter mode`() {
        viewModel.updateFilters(InvoiceFilters(minAmount = 50.0))
        viewModel.applyFilters()
        assertTrue(viewModel.isFilterModeActive.value)

        viewModel.restoreFilters(InvoiceFilters(), InvoiceFilters())

        assertFalse(viewModel.isFilterModeActive.value)
    }

    // ── updateStatistics ──────────────────────────────────────────────────────

    @Test
    fun `updateStatistics sets statistics correctly`() {
        val oldest = LocalDate.of(2024, 1, 1)
        val newest = LocalDate.of(2026, 3, 1)

        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 5.0,
            maxAmount = 350.0,
            oldestDate = oldest,
            newestDate = newest
        )

        val stats = viewModel.filterState.value.statistics
        assertEquals(5.0, stats.minAmount, 0.0)
        assertEquals(350.0, stats.maxAmount, 0.0)
        assertTrue(stats.oldestDateMillis > 0)
        assertTrue(stats.newestDateMillis > 0)
        assertTrue(stats.newestDateMillis > stats.oldestDateMillis)
    }

    @Test
    fun `updateStatistics with null dates sets millis to zero`() {
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 100.0,
            oldestDate = null,
            newestDate = null
        )

        val stats = viewModel.filterState.value.statistics
        assertEquals(0.0, stats.minAmount, 0.0)
        assertEquals(100.0, stats.maxAmount, 0.0)
        assertEquals(0L, stats.oldestDateMillis)
        assertEquals(0L, stats.newestDateMillis)
    }

    @Test
    fun `updateStatistics adjusts draft slider to new range`() {
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 200.0,
            oldestDate = null,
            newestDate = null
        )
        viewModel.updateFilters(InvoiceFilters(minAmount = 50.0, maxAmount = 200.0))

        // Llegan nuevos datos con un máximo mayor
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 500.0,
            oldestDate = null,
            newestDate = null
        )

        val filters = viewModel.filterState.value.filters
        assertEquals(50.0, filters.minAmount!!, 0.0)
        // maxAmount estaba en el techo anterior (200) -> se expande al nuevo (500)
        assertEquals(500.0, filters.maxAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics keeps custom max when not at ceiling`() {
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 200.0,
            oldestDate = null,
            newestDate = null
        )
        viewModel.updateFilters(InvoiceFilters(minAmount = 10.0, maxAmount = 100.0))

        // Llegan nuevos datos con un máximo mayor
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 500.0,
            oldestDate = null,
            newestDate = null
        )

        val filters = viewModel.filterState.value.filters
        // El usuario tenía un máximo personalizado de 100 (no en el techo) -> se mantiene
        assertEquals(100.0, filters.maxAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics expands draft minAmount when at floor`() {
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 10.0,
            maxAmount = 200.0,
            oldestDate = null,
            newestDate = null
        )
        viewModel.updateFilters(InvoiceFilters(minAmount = 10.0, maxAmount = 150.0))

        // Llegan nuevos datos con un mínimo menor
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 3.0,
            maxAmount = 200.0,
            oldestDate = null,
            newestDate = null
        )

        val filters = viewModel.filterState.value.filters
        // minAmount estaba en el suelo anterior (10) -> se expande al nuevo (3)
        assertEquals(3.0, filters.minAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics keeps custom min when not at floor`() {
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 5.0,
            maxAmount = 200.0,
            oldestDate = null,
            newestDate = null
        )
        viewModel.updateFilters(InvoiceFilters(minAmount = 30.0, maxAmount = 150.0))

        // Llegan nuevos datos con un mínimo menor
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 2.0,
            maxAmount = 200.0,
            oldestDate = null,
            newestDate = null
        )

        val filters = viewModel.filterState.value.filters
        // El usuario tenía un mínimo personalizado de 30 (no en el suelo 5) -> se mantiene
        assertEquals(30.0, filters.minAmount!!, 0.0)
    }

    // ── updateStatistics: expansion de filtros aplicados ──────────────────────

    @Test
    fun `updateStatistics expands applied maxAmount when at ceiling`() {
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 200.0,
            oldestDate = null,
            newestDate = null
        )
        viewModel.updateFilters(InvoiceFilters(minAmount = 0.0, maxAmount = 200.0))
        viewModel.applyFilters()

        // Llega refresh con facturas de importe mayor
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 400.0,
            oldestDate = null,
            newestDate = null
        )

        // El máximo aplicado estaba en el techo -> se expande
        assertEquals(400.0, viewModel.appliedFilters.value.maxAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics does not expand applied maxAmount when custom`() {
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 200.0,
            oldestDate = null,
            newestDate = null
        )
        viewModel.updateFilters(InvoiceFilters(minAmount = 0.0, maxAmount = 100.0))
        viewModel.applyFilters()

        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 400.0,
            oldestDate = null,
            newestDate = null
        )

        // El máximo aplicado era personalizado (100, no en el techo 200) -> se mantiene
        assertEquals(100.0, viewModel.appliedFilters.value.maxAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics expands applied minAmount when at floor`() {
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 10.0,
            maxAmount = 200.0,
            oldestDate = null,
            newestDate = null
        )
        viewModel.updateFilters(InvoiceFilters(minAmount = 10.0, maxAmount = 200.0))
        viewModel.applyFilters()

        // Llega refresh con factura de importe menor
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 3.0,
            maxAmount = 200.0,
            oldestDate = null,
            newestDate = null
        )

        // El mínimo aplicado estaba en el suelo -> se expande
        assertEquals(3.0, viewModel.appliedFilters.value.minAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics does not expand applied minAmount when custom`() {
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 5.0,
            maxAmount = 200.0,
            oldestDate = null,
            newestDate = null
        )
        viewModel.updateFilters(InvoiceFilters(minAmount = 30.0, maxAmount = 200.0))
        viewModel.applyFilters()

        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 2.0,
            maxAmount = 200.0,
            oldestDate = null,
            newestDate = null
        )

        // El mínimo aplicado era personalizado (30, no en el suelo 5) -> se mantiene
        assertEquals(30.0, viewModel.appliedFilters.value.minAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics expands applied dates when at boundaries`() {
        val oldOldest = LocalDate.of(2024, 1, 1)
        val oldNewest = LocalDate.of(2025, 12, 31)
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 100.0,
            oldestDate = oldOldest,
            newestDate = oldNewest
        )
        viewModel.updateFilters(InvoiceFilters(startDate = oldOldest, endDate = oldNewest))
        viewModel.applyFilters()

        // Nuevos datos extienden el rango
        val newOldest = LocalDate.of(2023, 6, 1)
        val newNewest = LocalDate.of(2026, 3, 1)
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 100.0,
            oldestDate = newOldest,
            newestDate = newNewest
        )

        // Las fechas estaban en los extremos -> se expanden al nuevo rango
        assertEquals(newOldest, viewModel.appliedFilters.value.startDate)
        assertEquals(newNewest, viewModel.appliedFilters.value.endDate)
    }

    @Test
    fun `updateStatistics does not expand applied dates when custom`() {
        val oldOldest = LocalDate.of(2024, 1, 1)
        val oldNewest = LocalDate.of(2025, 12, 31)
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 100.0,
            oldestDate = oldOldest,
            newestDate = oldNewest
        )

        val customStart = LocalDate.of(2024, 6, 1)
        val customEnd = LocalDate.of(2025, 6, 1)
        viewModel.updateFilters(InvoiceFilters(startDate = customStart, endDate = customEnd))
        viewModel.applyFilters()

        val newOldest = LocalDate.of(2023, 1, 1)
        val newNewest = LocalDate.of(2026, 6, 1)
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 100.0,
            oldestDate = newOldest,
            newestDate = newNewest
        )

        // Las fechas eran personalizadas (no en los extremos) -> se mantienen
        assertEquals(customStart, viewModel.appliedFilters.value.startDate)
        assertEquals(customEnd, viewModel.appliedFilters.value.endDate)
    }

    @Test
    fun `updateStatistics does not touch applied when no filters active`() {
        viewModel.updateStatistics(
            allInvoices = emptyList(),
            minAmount = 0.0,
            maxAmount = 200.0,
            oldestDate = null,
            newestDate = null
        )

        // Sin filtros aplicados -> applied permanece vacío
        assertEquals(InvoiceFilters(), viewModel.appliedFilters.value)
    }
}
