package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceSortCriteria
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SortInvoicesUseCaseTest {

    private lateinit var useCase: SortInvoicesUseCase

    private val invoice1 = Invoice(
        id = "1", status = InvoiceStatus.PAID, amount = 50.0,
        chargeDate = "15/01/2024", periodStart = "01/01/2024",
        periodEnd = "31/01/2024", supplyType = SupplyType.ELECTRICITY
    )
    private val invoice2 = Invoice(
        id = "2", status = InvoiceStatus.PENDING, amount = 100.0,
        chargeDate = "20/03/2024", periodStart = "01/03/2024",
        periodEnd = "31/03/2024", supplyType = SupplyType.ELECTRICITY
    )
    private val invoice3 = Invoice(
        id = "3", status = InvoiceStatus.PAID, amount = 75.0,
        chargeDate = "10/02/2024", periodStart = "01/02/2024",
        periodEnd = "29/02/2024", supplyType = SupplyType.ELECTRICITY
    )

    @Before
    fun setUp() {
        useCase = SortInvoicesUseCase()
    }

    // Ordena por fecha descendente: la mas reciente primero
    @Test
    fun `sort by date descending returns newest first`() {
        val result = useCase(listOf(invoice1, invoice2, invoice3), InvoiceSortCriteria.DATE_DESC)
        assertEquals(listOf("2", "3", "1"), result.map { it.id })
    }

    // Ordena por fecha ascendente: la mas antigua primero
    @Test
    fun `sort by date ascending returns oldest first`() {
        val result = useCase(listOf(invoice1, invoice2, invoice3), InvoiceSortCriteria.DATE_ASC)
        assertEquals(listOf("1", "3", "2"), result.map { it.id })
    }

    // Ordena por importe descendente: el mayor primero
    @Test
    fun `sort by amount descending returns highest first`() {
        val result = useCase(listOf(invoice1, invoice2, invoice3), InvoiceSortCriteria.AMOUNT_DESC)
        assertEquals(listOf("2", "3", "1"), result.map { it.id })
    }

    // Ordena por importe ascendente: el menor primero
    @Test
    fun `sort by amount ascending returns lowest first`() {
        val result = useCase(listOf(invoice1, invoice2, invoice3), InvoiceSortCriteria.AMOUNT_ASC)
        assertEquals(listOf("1", "3", "2"), result.map { it.id })
    }

    // Verifica que el criterio por defecto es DATE_DESC
    @Test
    fun `default criteria is DATE_DESC`() {
        val result = useCase(listOf(invoice1, invoice2, invoice3))
        assertEquals(listOf("2", "3", "1"), result.map { it.id })
    }

    // Verifica que una lista vacia devuelve lista vacia
    @Test
    fun `empty list returns empty list`() {
        val result = useCase(emptyList(), InvoiceSortCriteria.DATE_DESC)
        assertEquals(emptyList<Invoice>(), result)
    }

    // Verifica que un solo elemento se devuelve sin cambios
    @Test
    fun `single element list returns same list`() {
        val result = useCase(listOf(invoice1), InvoiceSortCriteria.DATE_DESC)
        assertEquals(listOf(invoice1), result)
    }

    // Verifica que una fecha invalida no crashea y se ordena al final
    @Test
    fun `invalid date format does not crash and sorts to beginning`() {
        val badDateInvoice = invoice1.copy(id = "bad", chargeDate = "invalid-date")
        val result = useCase(listOf(invoice2, badDateInvoice), InvoiceSortCriteria.DATE_DESC)
        assertEquals("2", result.first().id)
    }
}
