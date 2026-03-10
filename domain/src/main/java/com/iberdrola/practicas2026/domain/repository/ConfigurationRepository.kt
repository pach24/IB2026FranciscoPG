package com.iberdrola.practicas2026.FranciscoPG.domain.repository

interface ConfigurationRepository {
    fun isMockEnabled(): Boolean
    fun setMockEnabled(enabled: Boolean)
}