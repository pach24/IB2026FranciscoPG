package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

class FilterInvoicesUseCaseTest {

    private lateinit var useCase: FilterInvoicesUseCase
    private lateinit var baseList: List<Invoice>

    @Before
    fun setUp() {
        useCase = FilterInvoicesUseCase()

        baseList = listOf(
            createInvoice(id = "1", amount = 100.0, status = InvoiceStatus.PAID, date = "01/01/2025"),
            createInvoice(id = "2", amount = 200.0, status = InvoiceStatus.PENDING, date = "01/02/2025"),
            createInvoice(id = "3", amount = 300.0, status = InvoiceStatus.PAID, date = "01/03/2025")
        )
    }

    @Test
    fun `when filter by status returns only matching invoices`() {
        val filters = InvoiceFilters(
            filteredStatuses = setOf(InvoiceStatus.PAID)
        )

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(2, result.size)
        assertTrue(result.all { it.status == InvoiceStatus.PAID })
    }

    @Test
    fun `when filter by amount range returns invoices in range`() {
        val filters = InvoiceFilters(
            minAmount = 150.0,
            maxAmount = 250.0
        )

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(1, result.size)
        assertEquals(200.0, result[0].amount, 0.01)
    }

    @Test
    fun `when no filters returns all invoices`() {
        val filters = InvoiceFilters()

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(3, result.size)
    }

    @Test
    fun `when empty list returns empty list`() {
        val emptyList = emptyList<Invoice>()
        val filters = InvoiceFilters(filteredStatuses = setOf(InvoiceStatus.PAID))

        val result = useCase(invoices = emptyList, filters = filters)

        assertEquals(0, result.size)
    }

    @Test
    fun `when filter by date returns invoices in date range`() {
        val filters = InvoiceFilters(
            startDate = LocalDate.of(2025, 1, 15),
            endDate = LocalDate.of(2025, 2, 15)
        )

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(1, result.size)
        assertEquals("01/02/2025", result[0].chargeDate)
    }

    @Test
    fun `when multiple filters returns invoices meeting all criteria`() {
        val filters = InvoiceFilters(
            filteredStatuses = setOf(InvoiceStatus.PAID),
            startDate = LocalDate.of(2025, 1, 1),
            endDate = LocalDate.of(2025, 1, 31),
            minAmount = 50.0,
            maxAmount = 150.0
        )

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(1, result.size)
        with(result[0]) {
            assertEquals(InvoiceStatus.PAID, status)
            assertEquals(100.0, amount, 0.01)
            assertEquals("01/01/2025", chargeDate)
        }
    }

    @Test
    fun `when filter by multiple statuses returns matching invoices`() {
        val filters = InvoiceFilters(
            filteredStatuses = setOf(InvoiceStatus.PAID, InvoiceStatus.PENDING)
        )

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(3, result.size)
    }

    @Test
    fun `when filter by date range invoices without valid date are excluded`() {
        val listWithBadDate = baseList + createInvoice(
            id = "4", amount = 150.0, status = InvoiceStatus.PAID, date = ""
        )

        val filters = InvoiceFilters(
            startDate = LocalDate.of(2025, 1, 1),
            endDate = LocalDate.of(2025, 12, 31)
        )

        val result = useCase(invoices = listWithBadDate, filters = filters)

        assertEquals(3, result.size)
    }

    @Test
    fun `when amount equals range boundary is included`() {
        val filters = InvoiceFilters(
            minAmount = 100.0,
            maxAmount = 200.0
        )

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(2, result.size)
        assertTrue(result.any { it.amount == 100.0 })
        assertTrue(result.any { it.amount == 200.0 })
    }

    @Test
    fun `when filtered statuses empty returns all invoices`() {
        val filters = InvoiceFilters(filteredStatuses = emptySet())

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(3, result.size)
    }

    @Test
    fun `when date range is inverted returns empty list`() {
        val filters = InvoiceFilters(
            startDate = LocalDate.of(2025, 12, 31),
            endDate = LocalDate.of(2025, 1, 1)
        )

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(0, result.size)
    }

    // ── Casos límite: fechas parciales ────────────────────────────────────────

    @Test
    fun `when only startDate set filters from that date onwards`() {
        val filters = InvoiceFilters(startDate = LocalDate.of(2025, 2, 1))

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(2, result.size)
        assertTrue(result.none { it.id == "1" })
    }

    @Test
    fun `when only endDate set filters up to that date`() {
        val filters = InvoiceFilters(endDate = LocalDate.of(2025, 1, 31))

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
    }

    @Test
    fun `when chargeDate equals startDate invoice is included`() {
        val filters = InvoiceFilters(startDate = LocalDate.of(2025, 1, 1))

        val result = useCase(invoices = baseList, filters = filters)

        assertTrue(result.any { it.id == "1" })
    }

    @Test
    fun `when chargeDate equals endDate invoice is included`() {
        val filters = InvoiceFilters(endDate = LocalDate.of(2025, 3, 1))

        val result = useCase(invoices = baseList, filters = filters)

        assertTrue(result.any { it.id == "3" })
    }

    @Test
    fun `when chargeDate has partial format invoice is excluded from date filter`() {
        val listWithBadFormat = baseList + createInvoice(
            id = "5", amount = 50.0, status = InvoiceStatus.PAID, date = "01/2025"
        )
        val filters = InvoiceFilters(
            startDate = LocalDate.of(2020, 1, 1),
            endDate = LocalDate.of(2030, 12, 31)
        )

        val result = useCase(invoices = listWithBadFormat, filters = filters)

        assertEquals(3, result.size)
        assertTrue(result.none { it.id == "5" })
    }

    // ── Casos límite: importes parciales ──────────────────────────────────────

    @Test
    fun `when only minAmount set filters from that amount upwards`() {
        val filters = InvoiceFilters(minAmount = 200.0)

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(2, result.size)
        assertTrue(result.none { it.id == "1" })
    }

    @Test
    fun `when only maxAmount set filters up to that amount`() {
        val filters = InvoiceFilters(maxAmount = 100.0)

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
    }

    @Test
    fun `when minAmount is zero all invoices pass amount filter`() {
        val filters = InvoiceFilters(minAmount = 0.0)

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(3, result.size)
    }

    @Test
    fun `when invoice amount is zero and minAmount is zero it is included`() {
        val listWithZero = baseList + createInvoice(
            id = "6", amount = 0.0, status = InvoiceStatus.PAID, date = "01/04/2025"
        )
        val filters = InvoiceFilters(minAmount = 0.0, maxAmount = 50.0)

        val result = useCase(invoices = listWithZero, filters = filters)

        assertEquals(1, result.size)
        assertEquals("6", result[0].id)
    }

    @Test
    fun `when invoice has negative amount and no min filter it is excluded`() {
        val listWithNegative = baseList + createInvoice(
            id = "7", amount = -10.0, status = InvoiceStatus.PAID, date = "01/04/2025"
        )
        val filters = InvoiceFilters()

        val result = useCase(invoices = listWithNegative, filters = filters)

        // minAmount defaults to 0.0, so negative amounts are excluded
        assertEquals(3, result.size)
        assertTrue(result.none { it.id == "7" })
    }

    // ── Casos límite: estados ─────────────────────────────────────────────────

    @Test
    fun `when filter status matches no invoice returns empty`() {
        val filters = InvoiceFilters(filteredStatuses = setOf(InvoiceStatus.CANCELLED))

        val result = useCase(invoices = baseList, filters = filters)

        assertEquals(0, result.size)
    }

    private fun createInvoice(
        id: String,
        amount: Double,
        status: InvoiceStatus,
        date: String
    ) = Invoice(
        id = id,
        status = status,
        amount = amount,
        chargeDate = date,
        periodStart = "01/01/2025",
        periodEnd = "31/01/2025",
        supplyType = SupplyType.ELECTRICITY
    )
}
