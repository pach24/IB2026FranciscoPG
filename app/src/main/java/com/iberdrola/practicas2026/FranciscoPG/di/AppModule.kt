// Archivo: AppModule.kt (Ubicación: di)
package com.iberdrola.practicas2026.FranciscoPG.di

import android.content.Context
import co.infinum.retromock.Retromock
import com.google.gson.GsonBuilder
import com.iberdrola.practicas2026.data.network.InvoiceApiService
import com.iberdrola.practicas2026.FranciscoPG.data.repository.ConfigurationRepositoryImpl
import com.iberdrola.practicas2026.FranciscoPG.data.repository.InvoiceRepositoryImpl
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.InvoiceRepository
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetInvoicesUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetMockModeUseCase
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.SetMockModeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideRetrofit(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl("https://francisco-pacheco.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides @Singleton
    fun provideRetromock(retrofit: Retrofit, @ApplicationContext context: Context): Retromock {
        return Retromock.Builder()
            .retrofit(retrofit)
            .defaultBodyFactory { input -> context.assets.open(input) }
            .build()
    }

    @Provides @Singleton @Named("RealApi")
    fun provideRealApiService(retrofit: Retrofit): InvoiceApiService = retrofit.create(InvoiceApiService::class.java)

    @Provides @Singleton @Named("MockApi")
    fun provideMockApiService(retromock: Retromock): InvoiceApiService = retromock.create(InvoiceApiService::class.java)

    @Provides @Singleton
    fun provideConfigurationRepository(): ConfigurationRepository {
        return ConfigurationRepositoryImpl()
    }

    @Provides @Singleton
    fun provideInvoiceRepository(
        @Named("RealApi") realApi: InvoiceApiService,
        @Named("MockApi") mockApi: InvoiceApiService,
        configRepository: ConfigurationRepository
    ): InvoiceRepository {
        return InvoiceRepositoryImpl(realApi, mockApi, configRepository)
    }

    @Provides @Singleton
    fun provideGetInvoicesUseCase(repository: InvoiceRepository): GetInvoicesUseCase {
        return GetInvoicesUseCase(repository)
    }

    @Provides @Singleton
    fun provideGetMockModeUseCase(configRepository: ConfigurationRepository): GetMockModeUseCase {
        return GetMockModeUseCase(configRepository)
    }

    @Provides @Singleton
    fun provideSetMockModeUseCase(configRepository: ConfigurationRepository): SetMockModeUseCase {
        return SetMockModeUseCase(configRepository)
    }
}
