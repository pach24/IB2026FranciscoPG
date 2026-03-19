package com.iberdrola.practicas2026.FranciscoPG.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class InvoiceFiltersTest {

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
}
