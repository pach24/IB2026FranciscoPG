package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.FeedbackInteraction
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.FeedbackRepository
import javax.inject.Inject

class ShouldShowFeedbackPromptUseCase @Inject constructor(
    private val feedbackRepository: FeedbackRepository
) {
    suspend operator fun invoke(): Boolean {
        val exitCount = feedbackRepository.getExitCount()
        val lastInteraction = feedbackRepository.getLastInteraction()

        return when (lastInteraction) {
            FeedbackInteraction.RATED -> exitCount >= 10
            FeedbackInteraction.LATER -> exitCount >= 3
            FeedbackInteraction.DISMISSED -> exitCount >= 1
            FeedbackInteraction.NONE -> exitCount >= 1
        }
    }
}