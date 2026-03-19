package com.iberdrola.practicas2026.FranciscoPG.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class InvoiceStatusTest {

    // Verifica que PAID tiene el valor de API "Pagada"
    @Test
    fun `apiValue returns Pagada for PAID`() {
        assertEquals("Pagada", InvoiceStatus.PAID.apiValue)
    }

    // Verifica que PENDING tiene el valor de API "Pendiente de pago"
    @Test
    fun `apiValue returns Pendiente de pago for PENDING`() {
        assertEquals("Pendiente de pago", InvoiceStatus.PENDING.apiValue)
    }

    // Verifica que isPaid es true solo para PAID
    @Test
    fun `isPaid returns true for PAID`() {
        assertTrue(InvoiceStatus.PAID.isPaid)
    }

    // Verifica que isPaid es false para PENDING
    @Test
    fun `isPaid returns false for PENDING`() {
        assertFalse(InvoiceStatus.PENDING.isPaid)
    }

    // Verifica conversion de "Pagada" a PAID
    @Test
    fun `fromApiValue returns PAID for Pagada`() {
        assertEquals(InvoiceStatus.PAID, InvoiceStatus.fromApiValue("Pagada"))
    }

    // Verifica conversion de "Pendiente de pago" a PENDING
    @Test
    fun `fromApiValue returns PENDING for Pendiente de pago`() {
        assertEquals(InvoiceStatus.PENDING, InvoiceStatus.fromApiValue("Pendiente de pago"))
    }

    // Verifica que la conversion ignora mayusculas/minusculas
    @Test
    fun `fromApiValue is case insensitive`() {
        assertEquals(InvoiceStatus.PAID, InvoiceStatus.fromApiValue("pagada"))
        assertEquals(InvoiceStatus.PENDING, InvoiceStatus.fromApiValue("pendiente de pago"))
    }

    // Verifica que un valor desconocido devuelve PENDING por defecto
    @Test
    fun `fromApiValue defaults to PENDING for unknown value`() {
        assertEquals(InvoiceStatus.PENDING, InvoiceStatus.fromApiValue("Desconocido"))
        assertEquals(InvoiceStatus.PENDING, InvoiceStatus.fromApiValue(""))
    }
}
