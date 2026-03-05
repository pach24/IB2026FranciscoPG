package com.iberdrola.practicas2026.FranciscoPG.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Módulo Hilt global para proveer instancias únicas (Singletons) como Room, Retrofit o Retromock
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Expone el contexto de la aplicación para dependencias que no tienen acceso directo (ej. Room)
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }
}
