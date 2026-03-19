package com.iberdrola.practicas2026.data.repository

import com.iberdrola.practicas2026.FranciscoPG.data.repository.ConfigurationRepositoryImpl
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ConfigurationRepositoryImplTest {

    private lateinit var repository: ConfigurationRepositoryImpl

    @Before
    fun setUp() {
        repository = ConfigurationRepositoryImpl()
    }

    // Verifica que el modo mock esta activado por defecto
    @Test
    fun `mock is enabled by default`() {
        assertTrue(repository.isMockEnabled())
    }

    // Verifica que se puede desactivar el mock
    @Test
    fun `setMockEnabled to false disables mock`() = runTest {
        repository.setMockEnabled(false)
        assertFalse(repository.isMockEnabled())
    }

    // Verifica que se puede reactivar el mock tras desactivarlo
    @Test
    fun `setMockEnabled to true enables mock`() = runTest {
        repository.setMockEnabled(false)
        repository.setMockEnabled(true)
        assertTrue(repository.isMockEnabled())
    }

    // Verifica que alternar el modo varias veces funciona correctamente
    @Test
    fun `toggle mock mode multiple times`() = runTest {
        assertTrue(repository.isMockEnabled())
        repository.setMockEnabled(false)
        assertFalse(repository.isMockEnabled())
        repository.setMockEnabled(true)
        assertTrue(repository.isMockEnabled())
    }
}
