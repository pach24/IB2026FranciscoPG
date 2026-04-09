package com.iberdrola.practicas2026.FranciscoPG.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class SupplyTypeTest {

    // Verifica que ELECTRICITY tiene el valor de API "LUZ"
    @Test
    fun `apiValue returns LUZ for ELECTRICITY`() {
        assertEquals("LUZ", SupplyType.ELECTRICITY.apiValue)
    }

    // Verifica que GAS tiene el valor de API "GAS"
    @Test
    fun `apiValue returns GAS for GAS`() {
        assertEquals("GAS", SupplyType.GAS.apiValue)
    }

    // Verifica conversion de "LUZ" a ELECTRICITY
    @Test
    fun `fromApiValue returns ELECTRICITY for LUZ`() {
        assertEquals(SupplyType.ELECTRICITY, SupplyType.fromApiValue("LUZ"))
    }

    // Verifica conversion de "GAS" a GAS
    @Test
    fun `fromApiValue returns GAS for GAS`() {
        assertEquals(SupplyType.GAS, SupplyType.fromApiValue("GAS"))
    }

    // Verifica que la conversion ignora mayusculas/minusculas
    @Test
    fun `fromApiValue is case insensitive`() {
        assertEquals(SupplyType.ELECTRICITY, SupplyType.fromApiValue("luz"))
        assertEquals(SupplyType.GAS, SupplyType.fromApiValue("gas"))
        assertEquals(SupplyType.ELECTRICITY, SupplyType.fromApiValue("Luz"))
    }

    // Verifica que un valor desconocido devuelve ELECTRICITY por defecto
    @Test
    fun `fromApiValue defaults to ELECTRICITY for unknown value`() {
        assertEquals(SupplyType.ELECTRICITY, SupplyType.fromApiValue("AGUA"))
        assertEquals(SupplyType.ELECTRICITY, SupplyType.fromApiValue(""))
    }
}
