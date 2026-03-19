package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel

import com.iberdrola.practicas2026.FranciscoPG.core.error.ErrorClassifier
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.mapper.InvoiceUiMapper
import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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

@OptIn(ExperimentalCoroutinesApi::class)
class MyInvoicesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var getInvoicesUseCase: GetInvoicesUseCase
    private lateinit var invoiceUiMapper: InvoiceUiMapper
    private lateinit var errorClassifier: ErrorClassifier
    private lateinit var viewModel: MyInvoicesViewModel

    private val sampleInvoices = listOf(
        Invoice("1", InvoiceStatus.PAID, 50.0, "15/01/2024", "01/01/2024", "31/01/2024", SupplyType.ELECTRICITY),
        Invoice("2", InvoiceStatus.PENDING, 100.0, "20/03/2024", "01/03/2024", "31/03/2024", SupplyType.ELECTRICITY)
    )

    private val sampleUiModel = InvoiceUiModel(
        latestInvoice = LatestInvoiceUiModel(
            amount = "50.00 €", dateRange = "01/01/2024 - 31/01/2024",
            supplyTypeLabel = "Factura Luz", status = "Pagada", isPaid = true, iconRes = 0
        ),
        historyItems = listOf(
            InvoiceListItem.HeaderYear("2024"),
            InvoiceListItem.InvoiceItem("1", "15 de enero", "Factura Luz", "50.00 €", "Pagada", true),
            InvoiceListItem.InvoiceItem("2", "20 de marzo", "Factura Luz", "100.00 €", "Pendiente de pago", false)
        )
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getInvoicesUseCase = mockk()
        invoiceUiMapper = mockk()
        errorClassifier = ErrorClassifier()
        viewModel = MyInvoicesViewModel(getInvoicesUseCase, invoiceUiMapper, errorClassifier)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Verifica que ambos estados arrancan en Loading
    @Test
    fun `initial state is Loading`() {
        assertEquals(InvoiceListUiState.Loading, viewModel.listUiState.value)
        assertEquals(InvoiceUiState.Loading, viewModel.uiState.value)
    }

    // Con facturas, emite Success con el contador correcto
    @Test
    fun `fetchInvoices success with data emits Success`() = runTest {
        coEvery { getInvoicesUseCase(SupplyType.ELECTRICITY, any(), any()) } returns Result.success(sampleInvoices)
        every { invoiceUiMapper.map(sampleInvoices, SupplyType.ELECTRICITY) } returns sampleUiModel

        viewModel.fetchInvoices(SupplyType.ELECTRICITY)
        advanceUntilIdle()

        val listState = viewModel.listUiState.value
        assertTrue(listState is InvoiceListUiState.Success)
        assertEquals(2, (listState as InvoiceListUiState.Success).invoiceCount)
    }

    // Sin facturas, emite Empty
    @Test
    fun `fetchInvoices success with empty list emits Empty`() = runTest {
        coEvery { getInvoicesUseCase(SupplyType.ELECTRICITY, any(), any()) } returns Result.success(emptyList())

        viewModel.fetchInvoices(SupplyType.ELECTRICITY)
        advanceUntilIdle()

        assertEquals(InvoiceListUiState.Empty, viewModel.listUiState.value)
    }

    // Error de red se clasifica como ConnectionError
    @Test
    fun `fetchInvoices failure with network error emits ConnectionError`() = runTest {
        coEvery { getInvoicesUseCase(SupplyType.ELECTRICITY, any(), any()) } returns
                Result.failure(UnknownHostException("no host"))

        viewModel.fetchInvoices(SupplyType.ELECTRICITY)
        advanceUntilIdle()

        assertTrue(viewModel.listUiState.value is InvoiceListUiState.ConnectionError)
    }

    // Error generico se clasifica como ServerError
    @Test
    fun `fetchInvoices failure with generic error emits ServerError`() = runTest {
        coEvery { getInvoicesUseCase(SupplyType.ELECTRICITY, any(), any()) } returns
                Result.failure(RuntimeException("server error"))

        viewModel.fetchInvoices(SupplyType.ELECTRICITY)
        advanceUntilIdle()

        assertTrue(viewModel.listUiState.value is InvoiceListUiState.ServerError)
    }

    // Excepcion no controlada tambien emite estado de error
    @Test
    fun `fetchInvoices exception emits error state`() = runTest {
        coEvery { getInvoicesUseCase(SupplyType.GAS, any(), any()) } throws RuntimeException("crash")

        viewModel.fetchInvoices(SupplyType.GAS)
        advanceUntilIdle()

        assertTrue(viewModel.listUiState.value is InvoiceListUiState.ServerError)
    }

    // Pulsar funcionalidad no disponible activa el dialogo
    @Test
    fun `onFeatureNotAvailable sets dialog event to true`() {
        viewModel.onFeatureNotAvailable()
        assertTrue(viewModel.showDialogEvent.value)
    }

    // Aceptar el dialogo lo cierra
    @Test
    fun `onDialogHandled sets dialog event to false`() {
        viewModel.onFeatureNotAvailable()
        viewModel.onDialogHandled()
        assertFalse(viewModel.showDialogEvent.value)
    }

    // Verifica que forceRefresh se propaga al caso de uso
    @Test
    fun `fetchInvoices with forceRefresh passes flag through`() = runTest {
        coEvery { getInvoicesUseCase(SupplyType.ELECTRICITY, forceRefresh = true, any()) } returns
                Result.success(emptyList())

        viewModel.fetchInvoices(SupplyType.ELECTRICITY, forceRefresh = true)
        advanceUntilIdle()

        assertEquals(InvoiceListUiState.Empty, viewModel.listUiState.value)
    }

    // Llamadas rapidas consecutivas: solo el ultimo resultado se aplica (generation check)
    @Test
    fun `rapid fetchInvoices calls ignore stale results via generation check`() = runTest {
        // First call returns slowly with data
        coEvery { getInvoicesUseCase(SupplyType.ELECTRICITY, any(), any()) } returns Result.success(sampleInvoices)
        every { invoiceUiMapper.map(any(), any()) } returns sampleUiModel

        // Second call returns empty
        viewModel.fetchInvoices(SupplyType.ELECTRICITY)
        // Immediately call again — this increments generation
        coEvery { getInvoicesUseCase(SupplyType.ELECTRICITY, any(), any()) } returns Result.success(emptyList())
        viewModel.fetchInvoices(SupplyType.ELECTRICITY)
        advanceUntilIdle()

        // Should show Empty (second call), not Success (first call)
        assertEquals(InvoiceListUiState.Empty, viewModel.listUiState.value)
    }
}
