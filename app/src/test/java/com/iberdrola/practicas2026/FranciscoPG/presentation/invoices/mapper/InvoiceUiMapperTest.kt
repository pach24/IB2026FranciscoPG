package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.mapper

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class InvoiceUiMapperTest {

    private lateinit var mapper: InvoiceUiMapper

    private val invoice1 = Invoice(
        id = "1", status = InvoiceStatus.PAID, amount = 123.45,
        chargeDate = "15/01/2024", periodStart = "01/01/2024",
        periodEnd = "31/01/2024", supplyType = SupplyType.ELECTRICITY
    )
    private val invoice2 = Invoice(
        id = "2", status = InvoiceStatus.PENDING, amount = 67.89,
        chargeDate = "20/03/2024", periodStart = "01/03/2024",
        periodEnd = "31/03/2024", supplyType = SupplyType.ELECTRICITY
    )
    private val invoice3 = Invoice(
        id = "3", status = InvoiceStatus.PAID, amount = 200.0,
        chargeDate = "10/02/2025", periodStart = "01/02/2025",
        periodEnd = "28/02/2025", supplyType = SupplyType.GAS
    )

    @Before
    fun setUp() {
        mapper = InvoiceUiMapper()
    }

    // Verifica que una lista vacia no genera factura destacada ni historial
    @Test
    fun `map with empty list returns null latestInvoice and empty history`() {
        val result = mapper.map(emptyList(), SupplyType.ELECTRICITY)

        assertNull(result.latestInvoice)
        assertTrue(result.historyItems.isEmpty())
    }

    // Verifica que la primera factura se mapea como factura destacada con todos sus campos
    @Test
    fun `map returns first invoice as latestInvoice`() {
        val result = mapper.map(listOf(invoice1, invoice2), SupplyType.ELECTRICITY)

        assertNotNull(result.latestInvoice)
        assertEquals("%.2f €".format(123.45), result.latestInvoice!!.amount)
        assertEquals("01/01/2024 - 31/01/2024", result.latestInvoice!!.dateRange)
        assertEquals("Factura Luz", result.latestInvoice!!.supplyTypeLabel)
        assertEquals("Pagada", result.latestInvoice!!.status)
        assertTrue(result.latestInvoice!!.isPaid)
    }

    // Verifica que el label cambia a "Gas" cuando el tipo de suministro es GAS
    @Test
    fun `map for GAS uses correct label`() {
        val result = mapper.map(listOf(invoice3), SupplyType.GAS)

        assertEquals("Factura Gas", result.latestInvoice!!.supplyTypeLabel)
    }

    // Verifica la estructura del historial: 1 header de año + N items por año
    @Test
    fun `historyItems contain year headers and invoice items`() {
        val result = mapper.map(listOf(invoice1, invoice2), SupplyType.ELECTRICITY)

        assertEquals(3, result.historyItems.size)
        assertTrue(result.historyItems[0] is InvoiceListItem.HeaderYear)
        assertEquals("2024", (result.historyItems[0] as InvoiceListItem.HeaderYear).year)
        assertTrue(result.historyItems[1] is InvoiceListItem.InvoiceItem)
        assertTrue(result.historyItems[2] is InvoiceListItem.InvoiceItem)
    }

    // Verifica que facturas de distintos anios generan headers separados
    @Test
    fun `historyItems group by different years`() {
        val invoices = listOf(invoice1, invoice3)
        val result = mapper.map(invoices, SupplyType.ELECTRICITY)

        val headers = result.historyItems.filterIsInstance<InvoiceListItem.HeaderYear>()
        assertEquals(2, headers.size)
        assertEquals("2024", headers[0].year)
        assertEquals("2025", headers[1].year)
    }

    // Verifica que la fecha se formatea en espanol ("15 de enero")
    @Test
    fun `invoice item contains formatted date in Spanish`() {
        val result = mapper.map(listOf(invoice1), SupplyType.ELECTRICITY)

        val item = result.historyItems.filterIsInstance<InvoiceListItem.InvoiceItem>().first()
        assertEquals("15 de enero", item.date)
    }

    // Verifica el mes de marzo en español
    @Test
    fun `invoice item for March shows correct Spanish month`() {
        val result = mapper.map(listOf(invoice2), SupplyType.ELECTRICITY)

        val item = result.historyItems.filterIsInstance<InvoiceListItem.InvoiceItem>().first()
        assertEquals("20 de marzo", item.date)
    }

    // Verifica el formato del importe con 2 decimales y símbolo de euro
    @Test
    fun `invoice item contains formatted amount`() {
        val result = mapper.map(listOf(invoice1), SupplyType.ELECTRICITY)

        val item = result.historyItems.filterIsInstance<InvoiceListItem.InvoiceItem>().first()
        assertEquals("%.2f €".format(123.45), item.amount)
    }

    // Verifica que el estado e isPaid se mapean correctamente para facturas pendientes
    @Test
    fun `invoice item contains status text and isPaid flag`() {
        val result = mapper.map(listOf(invoice2), SupplyType.ELECTRICITY)

        val item = result.historyItems.filterIsInstance<InvoiceListItem.InvoiceItem>().first()
        assertEquals("Pendiente de pago", item.statusText)
        assertEquals(false, item.isPaid)
    }

    // Verifica que una fecha con formato invalido se devuelve tal cual sin crashear
    @Test
    fun `formatDateToSpanish handles invalid format gracefully`() {
        val badInvoice = invoice1.copy(chargeDate = "invalid")
        val result = mapper.map(listOf(badInvoice), SupplyType.ELECTRICITY)

        val item = result.historyItems.filterIsInstance<InvoiceListItem.InvoiceItem>().first()
        assertEquals("invalid", item.date)
    }
}
