package com.iberdrola.practicas2026.FranciscoPG.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class InvoiceExtensionsTest {

    private fun invoice(amount: Double = 0.0, chargeDate: String = "01/01/2025") = Invoice(
        id = "1", status = InvoiceStatus.PAID, amount = amount,
        chargeDate = chargeDate, periodStart = "", periodEnd = "",
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

    //  oldestDate

    @Test
    fun `oldestDate returns earliest date`() {
        val invoices = listOf(
            invoice(chargeDate = "15/06/2025"),
            invoice(chargeDate = "01/01/2024"),
            invoice(chargeDate = "30/12/2025")
        )
        assertEquals(LocalDate.of(2024, 1, 1), invoices.oldestDate())
    }

    @Test
    fun `oldestDate on empty list returns null`() {
        assertNull(emptyList<Invoice>().oldestDate())
    }

    @Test
    fun `oldestDate ignores invoices with invalid dates`() {
        val invoices = listOf(
            invoice(chargeDate = "invalid"),
            invoice(chargeDate = "01/03/2025")
        )
        assertEquals(LocalDate.of(2025, 3, 1), invoices.oldestDate())
    }

    @Test
    fun `oldestDate returns null when all dates are invalid`() {
        val invoices = listOf(invoice(chargeDate = ""), invoice(chargeDate = "bad"))
        assertNull(invoices.oldestDate())
    }

    // newestDate

    @Test
    fun `newestDate returns latest date`() {
        val invoices = listOf(
            invoice(chargeDate = "15/06/2025"),
            invoice(chargeDate = "01/01/2024"),
            invoice(chargeDate = "30/12/2025")
        )
        assertEquals(LocalDate.of(2025, 12, 30), invoices.newestDate())
    }

    @Test
    fun `newestDate on empty list returns null`() {
        assertNull(emptyList<Invoice>().newestDate())
    }

    @Test
    fun `newestDate ignores invoices with invalid dates`() {
        val invoices = listOf(
            invoice(chargeDate = "01/01/2025"),
            invoice(chargeDate = "not-a-date")
        )
        assertEquals(LocalDate.of(2025, 1, 1), invoices.newestDate())
    }
}
