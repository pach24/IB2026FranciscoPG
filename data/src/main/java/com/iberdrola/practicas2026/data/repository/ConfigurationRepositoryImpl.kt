// Archivo: ConfigurationRepositoryImpl.kt (Ubicación: data/repository)
package com.iberdrola.practicas2026.FranciscoPG.data.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import com.iberdrola.practicas2026.data.local.InvoiceDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConfigurationRepositoryImpl @Inject constructor(
    private val invoiceDao: InvoiceDao
) : ConfigurationRepository {
    // Por defecto el mock estará activado
    private var mockEnabled: Boolean = true

    override fun isMockEnabled(): Boolean = mockEnabled

    override suspend fun setMockEnabled(enabled: Boolean) {
        if (mockEnabled != enabled) {
            // Limpiar caché al cambiar entre mock y real para no mezclar datos
            invoiceDao.deleteAll()
        }
        mockEnabled = enabled
    }
}