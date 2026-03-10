// Archivo: ConfigurationRepositoryImpl.kt (Ubicación: data/repository)
package com.iberdrola.practicas2026.FranciscoPG.data.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigurationRepositoryImpl @Inject constructor() : ConfigurationRepository {
    // Por defecto el mock estará activado
    private var mockEnabled: Boolean = true

    override fun isMockEnabled(): Boolean = mockEnabled

    override fun setMockEnabled(enabled: Boolean) {
        mockEnabled = enabled
    }
}
