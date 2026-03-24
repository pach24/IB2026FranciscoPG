package com.iberdrola.practicas2026.FranciscoPG.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Módulo principal de Hilt.
 *
 * La configuración se ha dividido en módulos especializados:
 * - [NetworkModule]: Retrofit, OkHttp, Retromock y API services
 * - [DatabaseModule]: Room y DAOs
 * - [RepositoryModule]: Bindings de repositorios (interfaz → implementación)
 *
 * Los use cases, mappers y helpers usan @Inject constructor
 * y Hilt los resuelve automáticamente sin necesidad de @Provides.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule
