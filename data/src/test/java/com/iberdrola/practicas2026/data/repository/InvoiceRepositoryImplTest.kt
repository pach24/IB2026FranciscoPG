package com.iberdrola.practicas2026.data.repository

import com.iberdrola.practicas2026.FranciscoPG.data.repository.InvoiceRepositoryImpl
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import com.iberdrola.practicas2026.data.local.InvoiceDao
import com.iberdrola.practicas2026.data.local.InvoiceEntity
import com.iberdrola.practicas2026.data.model.InvoiceDto
import com.iberdrola.practicas2026.data.model.InvoiceListResponseDto
import com.iberdrola.practicas2026.data.network.InvoiceApiService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

class InvoiceRepositoryImplTest {

    private lateinit var realApi: InvoiceApiService
    private lateinit var mockApi: InvoiceApiService
    private lateinit var configRepo: ConfigurationRepository
    private lateinit var dao: InvoiceDao
    private lateinit var repository: InvoiceRepositoryImpl

    private val sampleDto = InvoiceDto(
        id = "1", descEstado = "Pagada", importeOrdenacion = 50.0,
        fechaCobro = "15/01/2024", fechaInicio = "01/01/2024",
        fechaFin = "31/01/2024", tipoSuministro = "LUZ"
    )

    private val sampleEntity = InvoiceEntity(
        id = "1", status = "Pagada", amount = 50.0,
        chargeDate = "15/01/2024", periodStart = "01/01/2024",
        periodEnd = "31/01/2024", supplyType = "LUZ"
    )

    @Before
    fun setUp() {
        realApi = mockk()
        mockApi = mockk()
        configRepo = mockk()
        dao = mockk()
        coEvery { dao.insertAll(any()) } just runs
        repository = InvoiceRepositoryImpl(realApi, mockApi, configRepo, dao)
    }

    // ── Mock mode ──────────────────────────────────────────────────────

    // En modo mock, obtiene facturas directamente de la API mock sin tocar Room
    @Test
    fun `mock mode returns data from mock API`() = runTest {
        every { configRepo.isMockEnabled() } returns true
        coEvery { mockApi.getInvoices("LUZ") } returns InvoiceListResponseDto(
            numFacturas = 1, facturas = listOf(sampleDto)
        )

        val result = repository.getInvoices(SupplyType.ELECTRICITY)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
        assertEquals("1", result.getOrThrow().first().id)
    }

    // En modo mock, filtra facturas por tipo de suministro
    @Test
    fun `mock mode filters by supply type`() = runTest {
        every { configRepo.isMockEnabled() } returns true
        val gasDto = sampleDto.copy(id = "2", tipoSuministro = "GAS")
        coEvery { mockApi.getInvoices("LUZ") } returns InvoiceListResponseDto(
            numFacturas = 2, facturas = listOf(sampleDto, gasDto)
        )

        val result = repository.getInvoices(SupplyType.ELECTRICITY)

        assertEquals(1, result.getOrThrow().size)
        assertEquals("1", result.getOrThrow().first().id)
    }

    // ── Real mode: first load (forceRefresh=false) ─────────────────────

    // Primera carga con Room vacio: llama a la API y guarda en Room
    @Test
    fun `real mode first load with empty Room fetches from API and caches`() = runTest {
        every { configRepo.isMockEnabled() } returns false
        coEvery { dao.getInvoicesBySupplyType("LUZ") } returns emptyList() andThen listOf(sampleEntity)
        coEvery { realApi.getInvoices("LUZ") } returns InvoiceListResponseDto(
            numFacturas = 1, facturas = listOf(sampleDto)
        )

        val result = repository.getInvoices(SupplyType.ELECTRICITY, forceRefresh = false)

        assertTrue(result.isSuccess)
        coVerify { dao.insertAll(any()) }
    }

    // Primera carga con datos en Room: no llama a la API
    @Test
    fun `real mode first load with cached data does not call API`() = runTest {
        every { configRepo.isMockEnabled() } returns false
        coEvery { dao.getInvoicesBySupplyType("LUZ") } returns listOf(sampleEntity)

        val result = repository.getInvoices(SupplyType.ELECTRICITY, forceRefresh = false)

        assertTrue(result.isSuccess)
        coVerify(exactly = 0) { realApi.getInvoices(any()) }
    }

    // Primera carga con Room vacio y API caida: devuelve error
    @Test
    fun `real mode first load with empty Room and API failure returns error`() = runTest {
        every { configRepo.isMockEnabled() } returns false
        coEvery { dao.getInvoicesBySupplyType("LUZ") } returns emptyList()
        coEvery { realApi.getInvoices("LUZ") } throws UnknownHostException("No internet")

        val result = repository.getInvoices(SupplyType.ELECTRICITY, forceRefresh = false)

        assertTrue(result.isFailure)
    }

    // ── Real mode: force refresh ───────────────────────────────────────

    // ForceRefresh sincroniza datos de la API en Room
    @Test
    fun `real mode forceRefresh syncs API data into Room`() = runTest {
        every { configRepo.isMockEnabled() } returns false
        coEvery { realApi.getInvoices("LUZ") } returns InvoiceListResponseDto(
            numFacturas = 1, facturas = listOf(sampleDto)
        )
        coEvery { dao.getInvoicesBySupplyType("LUZ") } returns listOf(sampleEntity)

        val result = repository.getInvoices(SupplyType.ELECTRICITY, forceRefresh = true)

        assertTrue(result.isSuccess)
        coVerify { dao.insertAll(any()) }
    }

    // SSOT: si la API falla en refresh, devuelve los datos que ya tiene Room
    @Test
    fun `real mode forceRefresh with API failure still returns Room data`() = runTest {
        every { configRepo.isMockEnabled() } returns false
        coEvery { realApi.getInvoices("LUZ") } throws UnknownHostException("No internet")
        coEvery { dao.getInvoicesBySupplyType("LUZ") } returns listOf(sampleEntity)

        val result = repository.getInvoices(SupplyType.ELECTRICITY, forceRefresh = true)

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
    }

    // Si la API falla en refresh y Room esta vacio, devuelve lista vacia (no error)
    @Test
    fun `real mode forceRefresh with API failure and empty Room returns empty list`() = runTest {
        every { configRepo.isMockEnabled() } returns false
        coEvery { realApi.getInvoices("LUZ") } throws UnknownHostException("No internet")
        coEvery { dao.getInvoicesBySupplyType("LUZ") } returns emptyList()

        val result = repository.getInvoices(SupplyType.ELECTRICITY, forceRefresh = true)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }
}
