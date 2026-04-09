package com.iberdrola.practicas2026.FranciscoPG.domain.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.model.FeedbackInteraction

interface FeedbackRepository {
    suspend fun getExitCount(): Int
    suspend fun setExitCount(count: Int)
    suspend fun getLastInteraction(): FeedbackInteraction
    suspend fun setLastInteraction(interaction: FeedbackInteraction)
}