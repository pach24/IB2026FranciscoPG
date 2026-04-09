// Archivo: GetMockModeUseCase.kt (Ubicación: domain/usecase)
package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import javax.inject.Inject

class GetMockModeUseCase @Inject constructor(
    private val configRepository: ConfigurationRepository
) {
    operator fun invoke(): Boolean {
        return configRepository.isMockEnabled()
    }
}
