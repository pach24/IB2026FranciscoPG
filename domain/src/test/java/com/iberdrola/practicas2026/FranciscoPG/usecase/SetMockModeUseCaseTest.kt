package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SetMockModeUseCaseTest {

    private lateinit var configRepository: ConfigurationRepository
    private lateinit var useCase: SetMockModeUseCase

    @Before
    fun setUp() {
        configRepository = mockk()
        coEvery { configRepository.setMockEnabled(any()) } just runs
        useCase = SetMockModeUseCase(configRepository)
    }

    // Verifica que activar mock delega al repositorio con true
    @Test
    fun `invoke with true enables mock mode`() = runTest {
        useCase(true)
        coVerify { configRepository.setMockEnabled(true) }
    }

    // Verifica que desactivar mock delega al repositorio con false
    @Test
    fun `invoke with false disables mock mode`() = runTest {
        useCase(false)
        coVerify { configRepository.setMockEnabled(false) }
    }
}
