package com.iberdrola.practicas2026.FranciscoPG.domain.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class DateValidatorTest {

    @Test
    fun `isValidRange returns true for valid range`() {
        assertTrue(
            DateValidator.isValidRange(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
            )
        )
    }

    @Test
    fun `isValidRange returns false for inverted range`() {
        assertFalse(
            DateValidator.isValidRange(
                LocalDate.of(2025, 12, 31),
                LocalDate.of(2025, 1, 1)
            )
        )
    }

    @Test
    fun `isValidRange returns true when start is null`() {
        assertTrue(DateValidator.isValidRange(null, LocalDate.of(2025, 1, 1)))
    }

    @Test
    fun `isValidRange returns true when end is null`() {
        assertTrue(DateValidator.isValidRange(LocalDate.of(2025, 1, 1), null))
    }

    @Test
    fun `isValidRange returns true when both null`() {
        assertTrue(DateValidator.isValidRange(null, null))
    }

    @Test
    fun `isValidRange returns true for equal dates`() {
        val date = LocalDate.of(2025, 6, 15)
        assertTrue(DateValidator.isValidRange(date, date))
    }

    @Test
    fun `isWithinBounds returns true when date is inside range`() {
        assertTrue(
            DateValidator.isWithinBounds(
                LocalDate.of(2025, 6, 15),
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
            )
        )
    }

    @Test
    fun `isWithinBounds returns false when date is before min`() {
        assertFalse(
            DateValidator.isWithinBounds(
                LocalDate.of(2024, 12, 31),
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
            )
        )
    }

    @Test
    fun `isWithinBounds returns false when date is after max`() {
        assertFalse(
            DateValidator.isWithinBounds(
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
            )
        )
    }

    @Test
    fun `isWithinBounds returns true when date is null`() {
        assertTrue(
            DateValidator.isWithinBounds(
                null,
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31)
            )
        )
    }

    @Test
    fun `isWithinBounds returns true when no bounds`() {
        assertTrue(
            DateValidator.isWithinBounds(
                LocalDate.of(2025, 6, 15),
                null,
                null
            )
        )
    }

    @Test
    fun `isWithinBounds returns true when date equals min`() {
        val date = LocalDate.of(2025, 1, 1)
        assertTrue(DateValidator.isWithinBounds(date, date, LocalDate.of(2025, 12, 31)))
    }

    @Test
    fun `isWithinBounds returns true when date equals max`() {
        val date = LocalDate.of(2025, 12, 31)
        assertTrue(DateValidator.isWithinBounds(date, LocalDate.of(2025, 1, 1), date))
    }
}
