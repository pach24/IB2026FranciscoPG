package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel

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
        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 100.0, oldestDate = null, newestDate = null)

        val stats = viewModel.filterState.value.statistics
        assertEquals(0.0, stats.minAmount, 0.0)
        assertEquals(100.0, stats.maxAmount, 0.0)
        assertEquals(0L, stats.oldestDateMillis)
        assertEquals(0L, stats.newestDateMillis)
    }

    @Test
    fun `updateStatistics adjusts draft slider to new range`() {
        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 200.0, oldestDate = null, newestDate = null)
        viewModel.updateFilters(InvoiceFilters(minAmount = 50.0, maxAmount = 200.0))

        // New data arrives with higher max
        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 500.0, oldestDate = null, newestDate = null)

        val filters = viewModel.filterState.value.filters
        assertEquals(50.0, filters.minAmount!!, 0.0)
        // maxAmount was at the old ceiling (200) -> expands to new ceiling (500)
        assertEquals(500.0, filters.maxAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics keeps custom max when not at ceiling`() {
        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 200.0, oldestDate = null, newestDate = null)
        viewModel.updateFilters(InvoiceFilters(minAmount = 10.0, maxAmount = 100.0))

        // New data arrives with higher max
        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 500.0, oldestDate = null, newestDate = null)

        val filters = viewModel.filterState.value.filters
        // User had a custom max of 100 (not at ceiling) -> stays at 100
        assertEquals(100.0, filters.maxAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics expands draft minAmount when at floor`() {
        viewModel.updateStatistics(minAmount = 10.0, maxAmount = 200.0, oldestDate = null, newestDate = null)
        viewModel.updateFilters(InvoiceFilters(minAmount = 10.0, maxAmount = 150.0))

        // New data arrives with lower min
        viewModel.updateStatistics(minAmount = 3.0, maxAmount = 200.0, oldestDate = null, newestDate = null)

        val filters = viewModel.filterState.value.filters
        // minAmount was at old floor (10) -> expands to new floor (3)
        assertEquals(3.0, filters.minAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics keeps custom min when not at floor`() {
        viewModel.updateStatistics(minAmount = 5.0, maxAmount = 200.0, oldestDate = null, newestDate = null)
        viewModel.updateFilters(InvoiceFilters(minAmount = 30.0, maxAmount = 150.0))

        // New data arrives with lower min
        viewModel.updateStatistics(minAmount = 2.0, maxAmount = 200.0, oldestDate = null, newestDate = null)

        val filters = viewModel.filterState.value.filters
        // User had custom min of 30 (not at floor 5) -> stays at 30
        assertEquals(30.0, filters.minAmount!!, 0.0)
    }

    // ── updateStatistics: expansion de filtros aplicados ──────────────────────

    @Test
    fun `updateStatistics expands applied maxAmount when at ceiling`() {
        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 200.0, oldestDate = null, newestDate = null)
        viewModel.updateFilters(InvoiceFilters(minAmount = 0.0, maxAmount = 200.0))
        viewModel.applyFilters()

        // Refresh brings new invoices with higher amounts
        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 400.0, oldestDate = null, newestDate = null)

        // Applied max was at ceiling -> should expand
        assertEquals(400.0, viewModel.appliedFilters.value.maxAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics does not expand applied maxAmount when custom`() {
        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 200.0, oldestDate = null, newestDate = null)
        viewModel.updateFilters(InvoiceFilters(minAmount = 0.0, maxAmount = 100.0))
        viewModel.applyFilters()

        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 400.0, oldestDate = null, newestDate = null)

        // Applied max was custom (100, not at ceiling 200) -> stays
        assertEquals(100.0, viewModel.appliedFilters.value.maxAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics expands applied minAmount when at floor`() {
        viewModel.updateStatistics(minAmount = 10.0, maxAmount = 200.0, oldestDate = null, newestDate = null)
        viewModel.updateFilters(InvoiceFilters(minAmount = 10.0, maxAmount = 200.0))
        viewModel.applyFilters()

        // Refresh brings invoice with lower amount
        viewModel.updateStatistics(minAmount = 3.0, maxAmount = 200.0, oldestDate = null, newestDate = null)

        // Applied min was at floor -> should expand
        assertEquals(3.0, viewModel.appliedFilters.value.minAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics does not expand applied minAmount when custom`() {
        viewModel.updateStatistics(minAmount = 5.0, maxAmount = 200.0, oldestDate = null, newestDate = null)
        viewModel.updateFilters(InvoiceFilters(minAmount = 30.0, maxAmount = 200.0))
        viewModel.applyFilters()

        viewModel.updateStatistics(minAmount = 2.0, maxAmount = 200.0, oldestDate = null, newestDate = null)

        // Applied min was custom (30, not at floor 5) -> stays
        assertEquals(30.0, viewModel.appliedFilters.value.minAmount!!, 0.0)
    }

    @Test
    fun `updateStatistics expands applied dates when at boundaries`() {
        val oldOldest = LocalDate.of(2024, 1, 1)
        val oldNewest = LocalDate.of(2025, 12, 31)
        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 100.0, oldestDate = oldOldest, newestDate = oldNewest)
        viewModel.updateFilters(InvoiceFilters(startDate = oldOldest, endDate = oldNewest))
        viewModel.applyFilters()

        // New data extends the range
        val newOldest = LocalDate.of(2023, 6, 1)
        val newNewest = LocalDate.of(2026, 3, 1)
        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 100.0, oldestDate = newOldest, newestDate = newNewest)

        // Dates were at boundaries -> should expand to new range
        assertEquals(newOldest, viewModel.appliedFilters.value.startDate)
        assertEquals(newNewest, viewModel.appliedFilters.value.endDate)
    }

    @Test
    fun `updateStatistics does not expand applied dates when custom`() {
        val oldOldest = LocalDate.of(2024, 1, 1)
        val oldNewest = LocalDate.of(2025, 12, 31)
        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 100.0, oldestDate = oldOldest, newestDate = oldNewest)

        val customStart = LocalDate.of(2024, 6, 1)
        val customEnd = LocalDate.of(2025, 6, 1)
        viewModel.updateFilters(InvoiceFilters(startDate = customStart, endDate = customEnd))
        viewModel.applyFilters()

        val newOldest = LocalDate.of(2023, 1, 1)
        val newNewest = LocalDate.of(2026, 6, 1)
        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 100.0, oldestDate = newOldest, newestDate = newNewest)

        // Dates were custom (not at boundaries) -> stay
        assertEquals(customStart, viewModel.appliedFilters.value.startDate)
        assertEquals(customEnd, viewModel.appliedFilters.value.endDate)
    }

    @Test
    fun `updateStatistics does not touch applied when no filters active`() {
        viewModel.updateStatistics(minAmount = 0.0, maxAmount = 200.0, oldestDate = null, newestDate = null)

        // No filters applied -> applied should remain empty
        assertEquals(InvoiceFilters(), viewModel.appliedFilters.value)
    }
}
