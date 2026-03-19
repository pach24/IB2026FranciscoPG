package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.repository.FeedbackRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class IncrementExitCounterUseCaseTest {

    private lateinit var feedbackRepository: FeedbackRepository
    private lateinit var useCase: IncrementExitCounterUseCase

    @Before
    fun setUp() {
        feedbackRepository = mockk()
        coEvery { feedbackRepository.setExitCount(any()) } just runs
        useCase = IncrementExitCounterUseCase(feedbackRepository)
    }

    // Incrementa de 0 a 1 (primera salida)
    @Test
    fun `increments exit count from 0 to 1`() = runTest {
        coEvery { feedbackRepository.getExitCount() } returns 0
        useCase()
        coVerify { feedbackRepository.setExitCount(1) }
    }

    // Incrementa de 5 a 6 (salida intermedia)
    @Test
    fun `increments exit count from 5 to 6`() = runTest {
        coEvery { feedbackRepository.getExitCount() } returns 5
        useCase()
        coVerify { feedbackRepository.setExitCount(6) }
    }

    // Incrementa de 9 a 10 (justo antes del umbral RATED)
    @Test
    fun `increments exit count from 9 to 10`() = runTest {
        coEvery { feedbackRepository.getExitCount() } returns 9
        useCase()
        coVerify { feedbackRepository.setExitCount(10) }
    }
}
