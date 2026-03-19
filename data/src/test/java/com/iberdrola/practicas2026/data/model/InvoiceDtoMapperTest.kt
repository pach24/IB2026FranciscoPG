package com.iberdrola.practicas2026.data.model

import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import org.junit.Assert.assertEquals
import org.junit.Test

class InvoiceDtoMapperTest {

    // Verifica que todos los campos del DTO se mapean correctamente al modelo de dominio
    @Test
    fun `toDomain maps all fields correctly`() {
        val dto = InvoiceDto(
            id = "INV-001",
            descEstado = "Pagada",
            importeOrdenacion = 123.45,
            fechaCobro = "15/01/2024",
            fechaInicio = "01/01/2024",
            fechaFin = "31/01/2024",
            tipoSuministro = "LUZ"
        )

        val invoice = dto.toDomain()

        assertEquals("INV-001", invoice.id)
        assertEquals(InvoiceStatus.PAID, invoice.status)
        assertEquals(123.45, invoice.amount, 0.001)
        assertEquals("15/01/2024", invoice.chargeDate)
        assertEquals("01/01/2024", invoice.periodStart)
        assertEquals("31/01/2024", invoice.periodEnd)
        assertEquals(SupplyType.ELECTRICITY, invoice.supplyType)
    }

    // Verifica el mapeo de GAS y estado pendiente
    @Test
    fun `toDomain maps GAS supply type`() {
        val dto = InvoiceDto(
            id = "INV-002",
            descEstado = "Pendiente de pago",
            importeOrdenacion = 80.0,
            fechaCobro = "20/02/2024",
            fechaInicio = "01/02/2024",
            fechaFin = "29/02/2024",
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
            descEstado = "Desconocido",
            importeOrdenacion = 0.0,
            fechaCobro = "01/01/2024",
            fechaInicio = "01/01/2024",
            fechaFin = "31/01/2024",
            tipoSuministro = "LUZ"
        )

        assertEquals(InvoiceStatus.PENDING, dto.toDomain().status)
    }

    // Verifica que un tipo de suministro desconocido se mapea como ELECTRICITY
    @Test
    fun `toDomain handles unknown supply type defaulting to ELECTRICITY`() {
        val dto = InvoiceDto(
            id = "INV-004",
            descEstado = "Pagada",
            importeOrdenacion = 0.0,
            fechaCobro = "01/01/2024",
            fechaInicio = "01/01/2024",
            fechaFin = "31/01/2024",
            tipoSuministro = "AGUA"
        )

        assertEquals(SupplyType.ELECTRICITY, dto.toDomain().supplyType)
    }
}
