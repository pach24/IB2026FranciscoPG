package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.FeedbackInteraction
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.FeedbackRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateFeedbackInteractionUseCaseTest {

    private lateinit var feedbackRepository: FeedbackRepository
    private lateinit var useCase: UpdateFeedbackInteractionUseCase

    @Before
    fun setUp() {
        feedbackRepository = mockk()
        coEvery { feedbackRepository.setExitCount(any()) } just runs
        coEvery { feedbackRepository.setLastInteraction(any()) } just runs
        useCase = UpdateFeedbackInteractionUseCase(feedbackRepository)
    }

    // Valorar resetea el contador para retrasar el proximo prompt
    @Test
    fun `RATED resets exit counter and saves interaction`() = runTest {
        useCase(FeedbackInteraction.RATED)

        coVerify { feedbackRepository.setExitCount(0) }
        coVerify { feedbackRepository.setLastInteraction(FeedbackInteraction.RATED) }
    }

    // "Mas tarde" resetea el contador para esperar 3 salidas mas
    @Test
    fun `LATER resets exit counter and saves interaction`() = runTest {
        useCase(FeedbackInteraction.LATER)

        coVerify { feedbackRepository.setExitCount(0) }
        coVerify { feedbackRepository.setLastInteraction(FeedbackInteraction.LATER) }
    }

    // Dismiss NO resetea el contador para que vuelva a aparecer en la siguiente salida
    @Test
    fun `DISMISSED does NOT reset exit counter but saves interaction`() = runTest {
        useCase(FeedbackInteraction.DISMISSED)

        coVerify(exactly = 0) { feedbackRepository.setExitCount(any()) }
        coVerify { feedbackRepository.setLastInteraction(FeedbackInteraction.DISMISSED) }
    }

    // NONE resetea el contador y guarda la interaccion
    @Test
    fun `NONE resets exit counter and saves interaction`() = runTest {
        useCase(FeedbackInteraction.NONE)

        coVerify { feedbackRepository.setExitCount(0) }
        coVerify { feedbackRepository.setLastInteraction(FeedbackInteraction.NONE) }
    }
}
