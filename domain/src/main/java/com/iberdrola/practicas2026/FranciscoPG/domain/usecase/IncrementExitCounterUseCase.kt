package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.repository.FeedbackRepository
import javax.inject.Inject

class IncrementExitCounterUseCase @Inject constructor(
    private val feedbackRepository: FeedbackRepository
) {
    suspend operator fun invoke() {
        val current = feedbackRepository.getExitCount()
        feedbackRepository.setExitCount(current + 1)
    }
}