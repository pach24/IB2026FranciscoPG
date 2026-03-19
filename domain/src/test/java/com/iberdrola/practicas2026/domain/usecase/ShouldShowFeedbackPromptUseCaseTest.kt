package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.FeedbackInteraction
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.FeedbackRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ShouldShowFeedbackPromptUseCaseTest {

    private lateinit var feedbackRepository: FeedbackRepository
    private lateinit var useCase: ShouldShowFeedbackPromptUseCase

    @Before
    fun setUp() {
        feedbackRepository = mockk()
        useCase = ShouldShowFeedbackPromptUseCase(feedbackRepository)
    }

    // NONE: umbral = 1. Con 0 salidas no muestra
    @Test
    fun `NONE interaction with exitCount 0 returns false`() = runTest {
        coEvery { feedbackRepository.getExitCount() } returns 0
        coEvery { feedbackRepository.getLastInteraction() } returns FeedbackInteraction.NONE
        assertFalse(useCase())
    }

    // NONE: umbral = 1. Con 1 salida ya muestra el prompt
    @Test
    fun `NONE interaction with exitCount 1 returns true`() = runTest {
        coEvery { feedbackRepository.getExitCount() } returns 1
        coEvery { feedbackRepository.getLastInteraction() } returns FeedbackInteraction.NONE
        assertTrue(useCase())
    }

    // DISMISSED: umbral = 1. Con 0 salidas no muestra
    @Test
    fun `DISMISSED interaction with exitCount 0 returns false`() = runTest {
        coEvery { feedbackRepository.getExitCount() } returns 0
        coEvery { feedbackRepository.getLastInteraction() } returns FeedbackInteraction.DISMISSED
        assertFalse(useCase())
    }

    // DISMISSED: umbral = 1. Con 1 salida ya muestra
    @Test
    fun `DISMISSED interaction with exitCount 1 returns true`() = runTest {
        coEvery { feedbackRepository.getExitCount() } returns 1
        coEvery { feedbackRepository.getLastInteraction() } returns FeedbackInteraction.DISMISSED
        assertTrue(useCase())
    }

    // LATER: umbral = 3. Con 2 salidas aun no muestra
    @Test
    fun `LATER interaction with exitCount 2 returns false`() = runTest {
        coEvery { feedbackRepository.getExitCount() } returns 2
        coEvery { feedbackRepository.getLastInteraction() } returns FeedbackInteraction.LATER
        assertFalse(useCase())
    }

    // LATER: umbral = 3. Con 3 salidas ya muestra
    @Test
    fun `LATER interaction with exitCount 3 returns true`() = runTest {
        coEvery { feedbackRepository.getExitCount() } returns 3
        coEvery { feedbackRepository.getLastInteraction() } returns FeedbackInteraction.LATER
        assertTrue(useCase())
    }

    // RATED: umbral = 10. Con 9 salidas aun no muestra
    @Test
    fun `RATED interaction with exitCount 9 returns false`() = runTest {
        coEvery { feedbackRepository.getExitCount() } returns 9
        coEvery { feedbackRepository.getLastInteraction() } returns FeedbackInteraction.RATED
        assertFalse(useCase())
    }

    // RATED: umbral = 10. Con 10 salidas ya muestra
    @Test
    fun `RATED interaction with exitCount 10 returns true`() = runTest {
        coEvery { feedbackRepository.getExitCount() } returns 10
        coEvery { feedbackRepository.getLastInteraction() } returns FeedbackInteraction.RATED
        assertTrue(useCase())
    }
}
