package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel

import com.iberdrola.practicas2026.FranciscoPG.core.error.ErrorClassifier
import com.iberdrola.practicas2026.FranciscoPG.domain.analytics.AnalyticsTracker
import com.iberdrola.practicas2026.FranciscoPG.domain.config.RemoteConfigProvider
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.FilterInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.mapper.InvoiceUiMapper
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoiceListItem
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoiceListUiState
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.InvoiceUiModel
import com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model.LatestInvoiceUiModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class InvoicesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getInvoicesUseCase: GetInvoicesUseCase
    private lateinit var filterInvoicesUseCase: FilterInvoicesUseCase
    private lateinit var invoiceUiMapper: InvoiceUiMapper
    private lateinit var errorClassifier: ErrorClassifier
    private lateinit var viewModel: InvoicesViewModel

    private val sampleInvoices = listOf(
        Invoice(
            id = "1",
            contractId = "LUZ_01",
            status = InvoiceStatus.PAID,
            amount = 50.0,
            chargeDate = LocalDate.of(2024, 1, 15),
            periodStart = LocalDate.of(2024, 1, 1),
            periodEnd = LocalDate.of(2024, 1, 31),
            supplyType = SupplyType.ELECTRICITY
        ),
        Invoice(
            id = "2",
            contractId = "LUZ_01",
            status = InvoiceStatus.PENDING,
            amount = 100.0,
            chargeDate = LocalDate.of(2024, 3, 20),
            periodStart = LocalDate.of(2024, 3, 1),
            periodEnd = LocalDate.of(2024, 3, 31),
            supplyType = SupplyType.ELECTRICITY
        )
    )

    private val sampleUiModel = InvoiceUiModel(
        latestInvoice = LatestInvoiceUiModel(
            amount = "50,00", currencySymbol = "€", dateRange = "01/01/2024 - 31/01/2024",
            supplyTypeLabel = "Factura Luz", statusText = "Pagada", status = InvoiceStatus.PAID, iconRes = 0
        ),
        historyItems = listOf(
            InvoiceListItem.HeaderYear("2024"),
            InvoiceListItem.InvoiceItem("1", "15 de enero", "Factura Luz", "50,00", "€", "Pagada", InvoiceStatus.PAID),
            InvoiceListItem.InvoiceItem("2", "20 de marzo", "Factura Luz", "100,00", "€", "Pendiente de pago", InvoiceStatus.PENDING)
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getInvoicesUseCase = mockk()
        filterInvoicesUseCase = FilterInvoicesUseCase()
        invoiceUiMapper = mockk()
        errorClassifier = ErrorClassifier()
        val remoteConfig = object : RemoteConfigProvider {
            override val isGasContractsEnabled = true
            override val gasContractsEnabledFlow = kotlinx.coroutines.flow.MutableStateFlow(true)
            override suspend fun fetchAndActivate() {}
        }
        val noOpTracker = object : AnalyticsTracker {
            override fun logScreenView(screenName: String) {}
            override fun logEvent(name: String, params: Map<String, String>) {}
        }
        viewModel = InvoicesViewModel(getInvoicesUseCase, filterInvoicesUseCase, invoiceUiMapper, errorClassifier, remoteConfig, noOpTracker)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() {
        val state = viewModel.uiState.value
        assertEquals(InvoiceListUiState.Loading, state.electricityState)
        assertEquals(InvoiceListUiState.Loading, state.gasState)
    }

    @Test
    fun `fetchInvoices success with data emits Success`() = runTest {
        coEvery { getInvoicesUseCase(SupplyType.ELECTRICITY, any(), any()) } returns Result.success(sampleInvoices)
        coEvery { getInvoicesUseCase(SupplyType.GAS, any(), any()) } returns Result.success(emptyList())
        every { invoiceUiMapper.map(sampleInvoices, SupplyType.ELECTRICITY) } returns sampleUiModel
        every { invoiceUiMapper.mapLatest(sampleInvoices, SupplyType.ELECTRICITY) } returns sampleUiModel.latestInvoice

        val collector = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.onEvent(InvoicesEvent.OnMockModeChanged(true))
        advanceUntilIdle()

        val elecState = viewModel.uiState.value.electricityState
        assertTrue(elecState is InvoiceListUiState.Success)
        assertEquals(2, (elecState as InvoiceListUiState.Success).invoiceCount)

        collector.cancel()
    }

    @Test
    fun `fetchInvoices success with empty list emits Empty`() = runTest {
        coEvery { getInvoicesUseCase(SupplyType.ELECTRICITY, any(), any()) } returns Result.success(emptyList())
        coEvery { getInvoicesUseCase(SupplyType.GAS, any(), any()) } returns Result.success(emptyList())

        val collector = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.onEvent(InvoicesEvent.OnMockModeChanged(true))
        advanceUntilIdle()

        assertEquals(InvoiceListUiState.Empty, viewModel.uiState.value.electricityState)

        collector.cancel()
    }

    @Test
    fun `fetchInvoices failure with network error emits ConnectionError`() = runTest {
        coEvery { getInvoicesUseCase(SupplyType.ELECTRICITY, any(), any()) } returns
                Result.failure(UnknownHostException("no host"))
        coEvery { getInvoicesUseCase(SupplyType.GAS, any(), any()) } returns Result.success(emptyList())

        val collector = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.onEvent(InvoicesEvent.OnMockModeChanged(true))
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.electricityState is InvoiceListUiState.ConnectionError)

        collector.cancel()
    }

    @Test
    fun `fetchInvoices failure with generic error emits ServerError`() = runTest {
        coEvery { getInvoicesUseCase(SupplyType.ELECTRICITY, any(), any()) } returns
                Result.failure(RuntimeException("server error"))
        coEvery { getInvoicesUseCase(SupplyType.GAS, any(), any()) } returns Result.success(emptyList())

        val collector = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.onEvent(InvoicesEvent.OnMockModeChanged(true))
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.electricityState is InvoiceListUiState.ServerError)

        collector.cancel()
    }

    @Test
    fun `fetchInvoices exception emits error state`() = runTest {
        coEvery { getInvoicesUseCase(SupplyType.ELECTRICITY, any(), any()) } returns Result.success(emptyList())
        coEvery { getInvoicesUseCase(SupplyType.GAS, any(), any()) } throws RuntimeException("crash")

        val collector = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.onEvent(InvoicesEvent.OnMockModeChanged(true))
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value.gasState is InvoiceListUiState.ServerError)

        collector.cancel()
    }

    @Test
    fun `OnFeatureNotAvailable sets banner to true`() = runTest {
        val collector = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.onEvent(InvoicesEvent.OnFeatureNotAvailable)
        advanceUntilIdle()
        assertTrue(viewModel.uiState.value.showBanner)

        collector.cancel()
    }

    @Test
    fun `OnBannerDismissed sets banner to false`() = runTest {
        val collector = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.onEvent(InvoicesEvent.OnFeatureNotAvailable)
        viewModel.onEvent(InvoicesEvent.OnBannerDismissed)
        advanceUntilIdle()
        assertFalse(viewModel.uiState.value.showBanner)

        collector.cancel()
    }

    @Test
    fun `getFilteredTotalCount returns combined count`() = runTest {
        coEvery { getInvoicesUseCase(SupplyType.ELECTRICITY, any(), any()) } returns Result.success(sampleInvoices)
        coEvery { getInvoicesUseCase(SupplyType.GAS, any(), any()) } returns Result.success(emptyList())
        every { invoiceUiMapper.map(sampleInvoices, SupplyType.ELECTRICITY) } returns sampleUiModel
        every { invoiceUiMapper.mapLatest(sampleInvoices, SupplyType.ELECTRICITY) } returns sampleUiModel.latestInvoice

        val collector = launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect {}
        }

        viewModel.onEvent(InvoicesEvent.OnMockModeChanged(true))
        advanceUntilIdle()

        assertEquals(2, viewModel.getFilteredTotalCount())

        collector.cancel()
    }
}
