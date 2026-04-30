package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceSortCriteria
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.InvoiceRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetInvoicesUseCaseTest {

    private lateinit var repository: InvoiceRepository
    private lateinit var sortUseCase: SortInvoicesUseCase
    private lateinit var useCase: GetInvoicesUseCase

    private val sampleInvoices = listOf(
        Invoice(id = "1", contractId = "LUZ_01", status = InvoiceStatus.PAID, amount = 50.0, chargeDate = "15/01/2024", periodStart = "01/01/2024", periodEnd = "31/01/2024", supplyType = SupplyType.ELECTRICITY),
        Invoice(id = "2", contractId = "LUZ_01", status = InvoiceStatus.PENDING, amount = 100.0, chargeDate = "20/03/2024", periodStart = "01/03/2024", periodEnd = "31/03/2024", supplyType = SupplyType.ELECTRICITY)
    )

    @Before
    fun setUp() {
        repository = mockk()
        sortUseCase = mockk()
        useCase = GetInvoicesUseCase(repository, sortUseCase)
    }

    // Verifica que el resultado se obtiene del repo y se ordena con SortInvoicesUseCase
    @Test
    fun `success returns sorted invoices`() = runTest {
        coEvery { repository.getInvoices(SupplyType.ELECTRICITY, false) } returns Result.success(sampleInvoices)
        every { sortUseCase(sampleInvoices, InvoiceSortCriteria.DATE_DESC) } returns sampleInvoices.reversed()

        val result = useCase(SupplyType.ELECTRICITY)

        assertTrue(result.isSuccess)
        assertEquals(listOf("2", "1"), result.getOrThrow().map { it.id })
        verify { sortUseCase(sampleInvoices, InvoiceSortCriteria.DATE_DESC) }
    }

    // Verifica que un error del repositorio se propaga sin modificar
    @Test
    fun `failure propagates error`() = runTest {
        val exception = RuntimeException("Network error")
        coEvery { repository.getInvoices(SupplyType.GAS, false) } returns Result.failure(exception)

        val result = useCase(SupplyType.GAS)

        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    // Verifica que forceRefresh se pasa correctamente al repositorio
    @Test
    fun `forceRefresh is passed to repository`() = runTest {
        coEvery { repository.getInvoices(SupplyType.ELECTRICITY, true) } returns Result.success(emptyList())
        every { sortUseCase(emptyList(), InvoiceSortCriteria.DATE_DESC) } returns emptyList()

        useCase(SupplyType.ELECTRICITY, forceRefresh = true)

        coVerify { repository.getInvoices(SupplyType.ELECTRICITY, true) }
    }

    // Verifica que un criterio de orden personalizado se pasa al SortUseCase
    @Test
    fun `custom sort criteria is passed to sort use case`() = runTest {
        coEvery { repository.getInvoices(SupplyType.ELECTRICITY, false) } returns Result.success(sampleInvoices)
        every { sortUseCase(sampleInvoices, InvoiceSortCriteria.AMOUNT_ASC) } returns sampleInvoices

        useCase(SupplyType.ELECTRICITY, sortCriteria = InvoiceSortCriteria.AMOUNT_ASC)

        verify { sortUseCase(sampleInvoices, InvoiceSortCriteria.AMOUNT_ASC) }
    }
}
