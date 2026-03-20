package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.ui.screens

import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.InvoiceListUiState
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel.LatestInvoiceUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ResolvePreferredTabIndexTest {

    private val successState = InvoiceListUiState.Success(
        latestInvoice = LatestInvoiceUiModel(
            amount = "50.00 €",
            dateRange = "01/01/2024 - 31/01/2024",
            supplyTypeLabel = "Factura",
            statusText = "Pagada",
            status = InvoiceStatus.PAID,
            iconRes = 0
        ),
        historyItems = emptyList(),
        invoiceCount = 1
    )

    // Electricidad vacía y Gas con datos → tab 1 (Gas)
    @Test
    fun `electricity empty and gas with data returns gas tab`() {
        val result = resolvePreferredTabIndex(
            electricityState = InvoiceListUiState.Empty,
            gasState = successState,
            bothLoaded = true
        )
        assertEquals(1, result)
    }

    // Ambas con datos → tab 0 (Electricidad)
    @Test
    fun `both with data returns electricity tab`() {
        val result = resolvePreferredTabIndex(
            electricityState = successState,
            gasState = successState,
            bothLoaded = true
        )
        assertEquals(0, result)
    }

    // Ambas vacías → tab 0 (Electricidad)
    @Test
    fun `both empty returns electricity tab`() {
        val result = resolvePreferredTabIndex(
            electricityState = InvoiceListUiState.Empty,
            gasState = InvoiceListUiState.Empty,
            bothLoaded = true
        )
        assertEquals(0, result)
    }

    // Electricidad con datos y Gas vacía → tab 0 (Electricidad)
    @Test
    fun `electricity with data and gas empty returns electricity tab`() {
        val result = resolvePreferredTabIndex(
            electricityState = successState,
            gasState = InvoiceListUiState.Empty,
            bothLoaded = true
        )
        assertEquals(0, result)
    }

    // Aún no han cargado ambos → tab 0 (Electricidad)
    @Test
    fun `not both loaded yet returns electricity tab`() {
        val result = resolvePreferredTabIndex(
            electricityState = InvoiceListUiState.Empty,
            gasState = successState,
            bothLoaded = false
        )
        assertEquals(0, result)
    }

    // Electricidad cargando → tab 0
    @Test
    fun `electricity loading returns electricity tab`() {
        val result = resolvePreferredTabIndex(
            electricityState = InvoiceListUiState.Loading,
            gasState = successState,
            bothLoaded = false
        )
        assertEquals(0, result)
    }

    // Electricidad vacía y Gas con error de servidor → tab 1 (Gas no está vacío, tiene error)
    @Test
    fun `electricity empty and gas server error returns gas tab`() {
        val result = resolvePreferredTabIndex(
            electricityState = InvoiceListUiState.Empty,
            gasState = InvoiceListUiState.ServerError("error"),
            bothLoaded = true
        )
        assertEquals(1, result)
    }

    // Electricidad vacía y Gas con error de conexión → tab 1 (Gas no está vacío, tiene error)
    @Test
    fun `electricity empty and gas connection error returns gas tab`() {
        val result = resolvePreferredTabIndex(
            electricityState = InvoiceListUiState.Empty,
            gasState = InvoiceListUiState.ConnectionError("no network"),
            bothLoaded = true
        )
        assertEquals(1, result)
    }

    // Electricidad con error de servidor y Gas con datos → tab 0
    @Test
    fun `electricity server error and gas with data returns electricity tab`() {
        val result = resolvePreferredTabIndex(
            electricityState = InvoiceListUiState.ServerError("error"),
            gasState = successState,
            bothLoaded = true
        )
        assertEquals(0, result)
    }
}
