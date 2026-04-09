package com.iberdrola.practicas2026.FranciscoPG.domain.repository

interface ConfigurationRepository {
    fun isMockEnabled(): Boolean
    suspend fun setMockEnabled(enabled: Boolean)
}