package com.iberdrola.practicas2026.FranciscoPG.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class InvoiceExtensionsTest {

    private fun invoice(
        amount: Double = 0.0,
        chargeDate: LocalDate = LocalDate.of(2025, 1, 1)
    ) = Invoice(
        id = "1",
        contractId = "LUZ_01",
        status = InvoiceStatus.PAID,
        amount = amount,
        chargeDate = chargeDate,
        periodStart = LocalDate.now(),
        periodEnd = LocalDate.now(),
        supplyType = SupplyType.ELECTRICITY
    )

    // maxAmount

    @Test
    fun `maxAmount returns highest amount`() {
        val invoices = listOf(invoice(amount = 50.0), invoice(amount = 200.0), invoice(amount = 100.0))
        assertEquals(200.0, invoices.maxAmount(), 0.01)
    }

    @Test
    fun `maxAmount on empty list returns zero`() {
        assertEquals(0.0, emptyList<Invoice>().maxAmount(), 0.01)
    }

    @Test
    fun `maxAmount with single invoice returns its amount`() {
        assertEquals(75.0, listOf(invoice(amount = 75.0)).maxAmount(), 0.01)
    }

    // minAmount

    @Test
    fun `minAmount returns lowest amount`() {
        val invoices = listOf(invoice(amount = 50.0), invoice(amount = 200.0), invoice(amount = 10.0))
        assertEquals(10.0, invoices.minAmount(), 0.01)
    }

    @Test
    fun `minAmount on empty list returns zero`() {
        assertEquals(0.0, emptyList<Invoice>().minAmount(), 0.01)
    }

    // oldestDate

    @Test
    fun `oldestDate returns earliest date`() {
        val invoices = listOf(
            invoice(chargeDate = LocalDate.of(2025, 6, 15)),
            invoice(chargeDate = LocalDate.of(2024, 1, 1)),
            invoice(chargeDate = LocalDate.of(2025, 12, 30))
        )
        assertEquals(LocalDate.of(2024, 1, 1), invoices.oldestDate())
    }

    @Test
    fun `oldestDate on empty list returns null`() {
        assertNull(emptyList<Invoice>().oldestDate())
    }

    // newestDate

    @Test
    fun `newestDate returns latest date`() {
        val invoices = listOf(
            invoice(chargeDate = LocalDate.of(2025, 6, 15)),
            invoice(chargeDate = LocalDate.of(2024, 1, 1)),
            invoice(chargeDate = LocalDate.of(2025, 12, 30))
        )
        assertEquals(LocalDate.of(2025, 12, 30), invoices.newestDate())
    }

    @Test
    fun `newestDate on empty list returns null`() {
        assertNull(emptyList<Invoice>().newestDate())
    }
}
