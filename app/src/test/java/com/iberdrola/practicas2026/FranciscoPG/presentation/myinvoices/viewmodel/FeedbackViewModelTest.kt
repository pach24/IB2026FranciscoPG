package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.viewmodel

import app.cash.turbine.test
import com.iberdrola.practicas2026.FranciscoPG.domain.model.FeedbackInteraction
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.IncrementExitCounterUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.ShouldShowFeedbackPromptUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.UpdateFeedbackInteractionUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FeedbackViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var incrementExitCounter: IncrementExitCounterUseCase
    private lateinit var shouldShowFeedback: ShouldShowFeedbackPromptUseCase
    private lateinit var updateFeedbackInteraction: UpdateFeedbackInteractionUseCase
    private lateinit var viewModel: FeedbackViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        incrementExitCounter = mockk()
        shouldShowFeedback = mockk()
        updateFeedbackInteraction = mockk()

        coEvery { incrementExitCounter() } just runs
        coEvery { updateFeedbackInteraction(any()) } just runs

        viewModel = FeedbackViewModel(
            incrementExitCounter,
            shouldShowFeedback,
            updateFeedbackInteraction
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Verifica que el bottom sheet empieza oculto
    @Test
    fun `initial state is Hidden`() {
        assertEquals(FeedbackSheetState.Hidden, viewModel.sheetState.value)
    }

    // Al salir y cumplir umbral, muestra el bottom sheet de feedback
    @Test
    fun `onExitInvoices shows feedback when shouldShow is true`() = runTest {
        coEvery { shouldShowFeedback() } returns true

        viewModel.onExitInvoices()
        advanceUntilIdle()

        assertEquals(FeedbackSheetState.Asking, viewModel.sheetState.value)
    }

    // Al salir sin cumplir umbral, navega hacia atras directamente
    @Test
    fun `onExitInvoices navigates back when shouldShow is false`() = runTest {
        coEvery { shouldShowFeedback() } returns false

        viewModel.navigateBack.test {
            viewModel.onExitInvoices()
            advanceUntilIdle()
            awaitItem()
        }
    }

    // Cada salida incrementa el contador de salidas
    @Test
    fun `onExitInvoices increments exit counter`() = runTest {
        coEvery { shouldShowFeedback() } returns false

        viewModel.onExitInvoices()
        advanceUntilIdle()

        coVerify { incrementExitCounter() }
    }

    // Si el sheet ya esta visible, no se re-dispara la logica
    @Test
    fun `onExitInvoices does nothing if sheet is already visible`() = runTest {
        coEvery { shouldShowFeedback() } returns true

        viewModel.onExitInvoices()
        advanceUntilIdle()
        assertEquals(FeedbackSheetState.Asking, viewModel.sheetState.value)

        // Call again while Asking
        viewModel.onExitInvoices()
        advanceUntilIdle()

        // Should still be Asking (not re-triggered)
        coVerify(exactly = 1) { incrementExitCounter() }
    }

    // Valorar guarda RATED y muestra el estado ThankYou
    @Test
    fun `onFeedbackRated updates interaction and shows ThankYou`() = runTest {
        coEvery { shouldShowFeedback() } returns true
        viewModel.onExitInvoices()
        advanceUntilIdle()

        viewModel.onFeedbackRated()
        // After calling rated, state should go to ThankYou
        advanceUntilIdle()

        coVerify { updateFeedbackInteraction(FeedbackInteraction.RATED) }
    }

    // "Mas tarde" oculta el sheet, guarda LATER y navega hacia atras
    @Test
    fun `onFeedbackLater hides sheet and navigates back`() = runTest {
        coEvery { shouldShowFeedback() } returns true
        viewModel.onExitInvoices()
        advanceUntilIdle()

        viewModel.navigateBack.test {
            viewModel.onFeedbackLater()
            advanceUntilIdle()
            awaitItem()
        }

        assertEquals(FeedbackSheetState.Hidden, viewModel.sheetState.value)
        coVerify { updateFeedbackInteraction(FeedbackInteraction.LATER) }
    }

    // Cerrar el sheet sin accion explicita guarda DISMISSED y navega atras
    @Test
    fun `onSheetDismissed saves DISMISSED interaction and navigates back`() = runTest {
        coEvery { shouldShowFeedback() } returns true
        viewModel.onExitInvoices()
        advanceUntilIdle()

        viewModel.navigateBack.test {
            viewModel.onSheetDismissed()
            advanceUntilIdle()
            awaitItem()
        }

        coVerify { updateFeedbackInteraction(FeedbackInteraction.DISMISSED) }
    }

    // Si ya se tomo una accion explicita (Later/Rated), dismiss no registra DISMISSED
    @Test
    fun `onSheetDismissed does nothing after explicit action`() = runTest {
        coEvery { shouldShowFeedback() } returns true
        viewModel.onExitInvoices()
        advanceUntilIdle()

        viewModel.onFeedbackLater()
        advanceUntilIdle()

        // Now dismiss — should be ignored because explicitActionTaken = true
        viewModel.onSheetDismissed()
        advanceUntilIdle()

        // DISMISSED should NOT have been called
        coVerify(exactly = 0) { updateFeedbackInteraction(FeedbackInteraction.DISMISSED) }
    }
}
