package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.FeedbackInteraction
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.FeedbackRepository
import javax.inject.Inject

class UpdateFeedbackInteractionUseCase @Inject constructor(
    private val feedbackRepository: FeedbackRepository
) {
    suspend operator fun invoke(interaction: FeedbackInteraction) {
        // CASO C (DISMISSED): NO resetear el contador para que vuelva a aparecer
        // en la siguiente salida. Solo RATED y LATER resetean.
        if (interaction != FeedbackInteraction.DISMISSED) {
            feedbackRepository.setExitCount(0)
        }
        feedbackRepository.setLastInteraction(interaction)
    }
}