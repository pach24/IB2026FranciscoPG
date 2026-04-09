package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetMockModeUseCaseTest {

    private lateinit var configRepository: ConfigurationRepository
    private lateinit var useCase: GetMockModeUseCase

    @Before
    fun setUp() {
        configRepository = mockk()
        useCase = GetMockModeUseCase(configRepository)
    }

    // Verifica que devuelve true cuando el mock esta habilitado
    @Test
    fun `returns true when mock is enabled`() {
        every { configRepository.isMockEnabled() } returns true
        assertTrue(useCase())
    }

    // Verifica que devuelve false cuando el mock esta deshabilitado
    @Test
    fun `returns false when mock is disabled`() {
        every { configRepository.isMockEnabled() } returns false
        assertFalse(useCase())
    }
}
