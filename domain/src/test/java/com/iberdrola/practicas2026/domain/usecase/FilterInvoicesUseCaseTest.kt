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
            filteredStatuses = setOf("Pagada")
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
        val filters = InvoiceFilters(filteredStatuses = setOf("Pagada"))

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
            filteredStatuses = setOf("Pagada"),
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
            filteredStatuses = setOf("Pagada", "Pendiente de pago")
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
