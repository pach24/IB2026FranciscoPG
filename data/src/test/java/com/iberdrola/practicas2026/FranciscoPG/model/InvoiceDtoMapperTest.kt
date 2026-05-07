package com.iberdrola.practicas2026.FranciscoPG.model

import com.iberdrola.practicas2026.FranciscoPG.data.model.InvoiceDto
import com.iberdrola.practicas2026.FranciscoPG.data.model.toDomain
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class InvoiceDtoMapperTest {

    private fun LocalDate.toMillis() = atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

    // Verifica que todos los campos del DTO se mapean correctamente al modelo de dominio
    @Test
    fun `toDomain maps all fields correctly`() {
        val chargeDate = LocalDate.of(2024, 1, 15)
        val periodStart = LocalDate.of(2024, 1, 1)
        val periodEnd = LocalDate.of(2024, 1, 31)

        val dto = InvoiceDto(
            id = "INV-001",
            contractId = "LUZ_01",
            descEstado = "Pagada",
            importeOrdenacion = 123.45,
            fechaCobro = chargeDate.toMillis(),
            fechaInicio = periodStart.toMillis(),
            fechaFin = periodEnd.toMillis(),
            tipoSuministro = "LUZ"
        )

        val invoice = dto.toDomain()

        assertEquals("INV-001", invoice.id)
        assertEquals(InvoiceStatus.PAID, invoice.status)
        assertEquals(123.45, invoice.amount, 0.001)
        assertEquals(chargeDate, invoice.chargeDate)
        assertEquals(periodStart, invoice.periodStart)
        assertEquals(periodEnd, invoice.periodEnd)
        assertEquals(SupplyType.ELECTRICITY, invoice.supplyType)
    }

    // Verifica el mapeo de GAS y estado pendiente
    @Test
    fun `toDomain maps GAS supply type`() {
        val dto = InvoiceDto(
            id = "INV-002",
            contractId = "GAS_01",
            descEstado = "Pendiente de pago",
            importeOrdenacion = 80.0,
            fechaCobro = LocalDate.of(2024, 2, 20).toMillis(),
            fechaInicio = LocalDate.of(2024, 2, 1).toMillis(),
            fechaFin = LocalDate.of(2024, 2, 29).toMillis(),
            tipoSuministro = "GAS"
        )

        val invoice = dto.toDomain()

        assertEquals(InvoiceStatus.PENDING, invoice.status)
        assertEquals(SupplyType.GAS, invoice.supplyType)
    }

    // Verifica que un estado desconocido se mapea como PENDING
    @Test
    fun `toDomain handles unknown status defaulting to PENDING`() {
        val dto = InvoiceDto(
            id = "INV-003",
            contractId = "LUZ_01",
            descEstado = "Desconocido",
            importeOrdenacion = 0.0,
            fechaCobro = 0L,
            fechaInicio = 0L,
            fechaFin = 0L,
            tipoSuministro = "LUZ"
        )

        assertEquals(InvoiceStatus.PENDING, dto.toDomain().status)
    }

    // Verifica que un tipo de suministro desconocido se mapea como ELECTRICITY
    @Test
    fun `toDomain handles unknown supply type defaulting to ELECTRICITY`() {
        val dto = InvoiceDto(
            id = "INV-004",
            contractId = "LUZ_01",
            descEstado = "Pagada",
            importeOrdenacion = 0.0,
            fechaCobro = 0L,
            fechaInicio = 0L,
            fechaFin = 0L,
            tipoSuministro = "AGUA"
        )

        assertEquals(SupplyType.ELECTRICITY, dto.toDomain().supplyType)
    }
}
