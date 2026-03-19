package com.iberdrola.practicas2026.data.local

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import org.junit.Assert.assertEquals
import org.junit.Test

class InvoiceEntityMapperTest {

    // Verifica que todos los campos de la entidad se mapean al dominio
    @Test
    fun `toDomain maps entity to domain model correctly`() {
        val entity = InvoiceEntity(
            id = "E-001",
            status = "Pagada",
            amount = 99.99,
            chargeDate = "10/05/2024",
            periodStart = "01/05/2024",
            periodEnd = "31/05/2024",
            supplyType = "LUZ"
        )

        val invoice = entity.toDomain()

        assertEquals("E-001", invoice.id)
        assertEquals(InvoiceStatus.PAID, invoice.status)
        assertEquals(99.99, invoice.amount, 0.001)
        assertEquals("10/05/2024", invoice.chargeDate)
        assertEquals("01/05/2024", invoice.periodStart)
        assertEquals("31/05/2024", invoice.periodEnd)
        assertEquals(SupplyType.ELECTRICITY, invoice.supplyType)
    }

    // Verifica el mapeo de una entidad de tipo GAS con estado pendiente
    @Test
    fun `toDomain maps GAS entity`() {
        val entity = InvoiceEntity(
            id = "E-002",
            status = "Pendiente de pago",
            amount = 45.0,
            chargeDate = "15/06/2024",
            periodStart = "01/06/2024",
            periodEnd = "30/06/2024",
            supplyType = "GAS"
        )

        val invoice = entity.toDomain()

        assertEquals(InvoiceStatus.PENDING, invoice.status)
        assertEquals(SupplyType.GAS, invoice.supplyType)
    }

    // Verifica que el dominio se convierte a entidad con los apiValues correctos
    @Test
    fun `toEntity maps domain model to entity correctly`() {
        val invoice = Invoice(
            id = "D-001",
            status = InvoiceStatus.PAID,
            amount = 150.0,
            chargeDate = "20/07/2024",
            periodStart = "01/07/2024",
            periodEnd = "31/07/2024",
            supplyType = SupplyType.GAS
        )

        val entity = invoice.toEntity()

        assertEquals("D-001", entity.id)
        assertEquals("Pagada", entity.status)
        assertEquals(150.0, entity.amount, 0.001)
        assertEquals("20/07/2024", entity.chargeDate)
        assertEquals("01/07/2024", entity.periodStart)
        assertEquals("31/07/2024", entity.periodEnd)
        assertEquals("GAS", entity.supplyType)
    }

    // Verifica que convertir a entity y volver a dominio no pierde datos
    @Test
    fun `roundtrip toEntity then toDomain preserves data`() {
        val original = Invoice(
            id = "RT-001",
            status = InvoiceStatus.PENDING,
            amount = 200.50,
            chargeDate = "25/08/2024",
            periodStart = "01/08/2024",
            periodEnd = "31/08/2024",
            supplyType = SupplyType.ELECTRICITY
        )

        val roundtripped = original.toEntity().toDomain()

        assertEquals(original, roundtripped)
    }
}
