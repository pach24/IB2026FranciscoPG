package com.iberdrola.practicas2026.FranciscoPG.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class InvoiceFiltersTest {

    // ── normalize ─────────────────────────────────────────────────────────────

    @Test
    fun `normalize swaps inverted dates`() {
        val filters = InvoiceFilters(
            startDate = LocalDate.of(2025, 12, 31),
            endDate = LocalDate.of(2025, 1, 1)
        )

        val normalized = filters.normalize()

        assertEquals(LocalDate.of(2025, 1, 1), normalized.startDate)
        assertEquals(LocalDate.of(2025, 12, 31), normalized.endDate)
    }

    @Test
    fun `normalize swaps inverted amounts`() {
        val filters = InvoiceFilters(
            minAmount = 500.0,
            maxAmount = 100.0
        )

        val normalized = filters.normalize()

        assertEquals(100.0, normalized.minAmount!!, 0.01)
        assertEquals(500.0, normalized.maxAmount!!, 0.01)
    }

    @Test
    fun `normalize does not change valid filters`() {
        val filters = InvoiceFilters(
            startDate = LocalDate.of(2025, 1, 1),
            endDate = LocalDate.of(2025, 12, 31),
            minAmount = 10.0,
            maxAmount = 500.0
        )

        val normalized = filters.normalize()

        assertEquals(filters, normalized)
    }

    @Test
    fun `normalize handles null dates`() {
        val filters = InvoiceFilters(
            startDate = null,
            endDate = LocalDate.of(2025, 12, 31)
        )

        val normalized = filters.normalize()

        assertNull(normalized.startDate)
        assertEquals(LocalDate.of(2025, 12, 31), normalized.endDate)
    }

    @Test
    fun `normalize handles null amounts`() {
        val filters = InvoiceFilters(
            minAmount = null,
            maxAmount = 500.0
        )

        val normalized = filters.normalize()

        assertNull(normalized.minAmount)
        assertEquals(500.0, normalized.maxAmount!!, 0.01)
    }

    @Test
    fun `normalize is idempotent with all nulls`() {
        val filters = InvoiceFilters()

        val normalized = filters.normalize()

        assertEquals(filters, normalized)
    }

    @Test
    fun `normalize keeps same dates when startDate equals endDate`() {
        val date = LocalDate.of(2025, 6, 15)
        val filters = InvoiceFilters(startDate = date, endDate = date)

        val normalized = filters.normalize()

        assertEquals(date, normalized.startDate)
        assertEquals(date, normalized.endDate)
    }

    @Test
    fun `normalize keeps same amounts when minAmount equals maxAmount`() {
        val filters = InvoiceFilters(minAmount = 100.0, maxAmount = 100.0)

        val normalized = filters.normalize()

        assertEquals(100.0, normalized.minAmount!!, 0.01)
        assertEquals(100.0, normalized.maxAmount!!, 0.01)
    }

    @Test
    fun `normalize with minAmount zero and maxAmount null stays unchanged`() {
        val filters = InvoiceFilters(minAmount = 0.0, maxAmount = null)

        val normalized = filters.normalize()

        assertEquals(0.0, normalized.minAmount!!, 0.01)
        assertNull(normalized.maxAmount)
    }

    // ── activeCount ───────────────────────────────────────────────────────────

    @Test
    fun `activeCount is zero when no filters set`() {
        assertEquals(0, InvoiceFilters().activeCount)
    }

    @Test
    fun `activeCount counts date as one when only startDate set`() {
        val filters = InvoiceFilters(startDate = LocalDate.of(2025, 1, 1))

        assertEquals(1, filters.activeCount)
    }

    @Test
    fun `activeCount counts date as one when only endDate set`() {
        val filters = InvoiceFilters(endDate = LocalDate.of(2025, 12, 31))

        assertEquals(1, filters.activeCount)
    }

    @Test
    fun `activeCount counts date as one when both dates set`() {
        val filters = InvoiceFilters(
            startDate = LocalDate.of(2025, 1, 1),
            endDate = LocalDate.of(2025, 12, 31)
        )

        assertEquals(1, filters.activeCount)
    }

    @Test
    fun `activeCount does not count minAmount zero`() {
        val filters = InvoiceFilters(minAmount = 0.0)

        assertEquals(0, filters.activeCount)
    }

    @Test
    fun `activeCount counts minAmount when greater than zero`() {
        val filters = InvoiceFilters(minAmount = 0.001)

        assertEquals(1, filters.activeCount)
    }

    @Test
    fun `activeCount counts maxAmount when set`() {
        val filters = InvoiceFilters(maxAmount = 500.0)

        assertEquals(1, filters.activeCount)
    }

    @Test
    fun `activeCount counts statuses when not empty`() {
        val filters = InvoiceFilters(filteredStatuses = setOf("Pagada"))

        assertEquals(1, filters.activeCount)
    }

    @Test
    fun `activeCount returns max when all filters active`() {
        val filters = InvoiceFilters(
            startDate = LocalDate.of(2025, 1, 1),
            endDate = LocalDate.of(2025, 12, 31),
            minAmount = 10.0,
            maxAmount = 500.0,
            filteredStatuses = setOf("Pagada", "Pendiente de pago")
        )

        assertEquals(4, filters.activeCount)
    }
}
