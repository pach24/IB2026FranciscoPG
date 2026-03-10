// Archivo: SetMockModeUseCase.kt (Ubicación: domain/usecase)
package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import javax.inject.Inject

class SetMockModeUseCase @Inject constructor(
    private val configRepository: ConfigurationRepository
) {
    operator fun invoke(enabled: Boolean) {
        configRepository.setMockEnabled(enabled)
    }
}
