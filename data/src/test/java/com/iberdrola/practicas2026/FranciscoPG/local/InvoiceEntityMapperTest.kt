package com.iberdrola.practicas2026.FranciscoPG.local

import com.iberdrola.practicas2026.FranciscoPG.data.local.InvoiceEntity
import com.iberdrola.practicas2026.FranciscoPG.data.local.toDomain
import com.iberdrola.practicas2026.FranciscoPG.data.local.toEntity
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class InvoiceEntityMapperTest {

    private fun LocalDate.toMillis() = atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    // Verifica que todos los campos de la entidad se mapean al dominio
    @Test
    fun `toDomain maps entity to domain model correctly`() {
        val chargeDate = LocalDate.of(2024, 5, 10)
        val periodStart = LocalDate.of(2024, 5, 1)
        val periodEnd = LocalDate.of(2024, 5, 31)

        val entity = InvoiceEntity(
            id = "E-001",
            contractId = "LUZ_01",
            status = "Pagada",
            amount = 99.99,
            chargeDate = chargeDate.toMillis(),
            periodStart = periodStart.toMillis(),
            periodEnd = periodEnd.toMillis(),
            supplyType = "LUZ"
        )

        val invoice = entity.toDomain()

        assertEquals("E-001", invoice.id)
        assertEquals("LUZ_01", invoice.contractId)
        assertEquals(InvoiceStatus.PAID, invoice.status)
        assertEquals(99.99, invoice.amount, 0.001)
        assertEquals(chargeDate, invoice.chargeDate)
        assertEquals(periodStart, invoice.periodStart)
        assertEquals(periodEnd, invoice.periodEnd)
        assertEquals(SupplyType.ELECTRICITY, invoice.supplyType)
    }

    // Verifica el mapeo de una entidad de tipo GAS con estado pendiente
    @Test
    fun `toDomain maps GAS entity`() {
        val entity = InvoiceEntity(
            id = "E-002",
            contractId = "GAS_01",
            status = "Pendiente de pago",
            amount = 45.0,
            chargeDate = LocalDate.of(2024, 6, 15).toMillis(),
            periodStart = LocalDate.of(2024, 6, 1).toMillis(),
            periodEnd = LocalDate.of(2024, 6, 30).toMillis(),
            supplyType = "GAS"
        )

        val invoice = entity.toDomain()

        assertEquals(InvoiceStatus.PENDING, invoice.status)
        assertEquals(SupplyType.GAS, invoice.supplyType)
    }

    // Verifica que el dominio se convierte a entidad con los apiValues correctos
    @Test
    fun `toEntity maps domain model to entity correctly`() {
        val chargeDate = LocalDate.of(2024, 7, 20)
        val periodStart = LocalDate.of(2024, 7, 1)
        val periodEnd = LocalDate.of(2024, 7, 31)

        val invoice = Invoice(
            id = "D-001",
            contractId = "GAS_01",
            status = InvoiceStatus.PAID,
            amount = 150.0,
            chargeDate = chargeDate,
            periodStart = periodStart,
            periodEnd = periodEnd,
            supplyType = SupplyType.GAS
        )

        val entity = invoice.toEntity()

        assertEquals("D-001", entity.id)
        assertEquals("GAS_01", entity.contractId)
        assertEquals("Pagada", entity.status)
        assertEquals(150.0, entity.amount, 0.001)
        assertEquals(chargeDate.toMillis(), entity.chargeDate)
        assertEquals(periodStart.toMillis(), entity.periodStart)
        assertEquals(periodEnd.toMillis(), entity.periodEnd)
        assertEquals("GAS", entity.supplyType)
    }

    // Verifica que convertir a entity y volver a dominio no pierde datos
    @Test
    fun `roundtrip toEntity then toDomain preserves data`() {
        val original = Invoice(
            id = "RT-001",
            contractId = "LUZ_01",
            status = InvoiceStatus.PENDING,
            amount = 200.50,
            chargeDate = LocalDate.of(2024, 8, 25),
            periodStart = LocalDate.of(2024, 8, 1),
            periodEnd = LocalDate.of(2024, 8, 31),
            supplyType = SupplyType.ELECTRICITY
        )

        val roundtripped = original.toEntity().toDomain()

        assertEquals(original, roundtripped)
    }
}
