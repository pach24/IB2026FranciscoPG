package com.iberdrola.practicas2026.FranciscoPG.di

import android.content.Context
import co.infinum.retromock.Retromock
import com.google.gson.GsonBuilder
import com.iberdrola.practicas2026.data.network.InvoiceApiService
import com.iberdrola.practicas2026.FranciscoPG.data.repository.InvoiceRepositoryImpl
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.InvoiceRepository
import com.iberdrola.practicas2026.FranciscoPG.domain.usecase.GetInvoicesUseCase
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

    // 1. RETROFIT
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        // Configuramos Gson en modo lenient para mayor tolerancia en el parseo
        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .baseUrl("https://api.iberdrola.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // 2. RETROMOCK
    @Provides
    @Singleton
    fun provideRetromock(
        retrofit: Retrofit,
        @ApplicationContext context: Context
    ): Retromock {
        return Retromock.Builder()
            .retrofit(retrofit)
            // Implementamos un BodyFactory para leer los mocks desde la carpeta assets
            .defaultBodyFactory { input -> context.assets.open(input) }
            .build()
    }

    // 3. SERVICIOS API
    @Provides
    @Singleton
    @Named("RealApi")
    fun provideRealApiService(retrofit: Retrofit): InvoiceApiService {
        return retrofit.create(InvoiceApiService::class.java)
    }

    @Provides
    @Singleton
    @Named("MockApi")
    fun provideMockApiService(retromock: Retromock): InvoiceApiService {
        return retromock.create(InvoiceApiService::class.java)
    }

    // 4. REPOSITORY
    @Provides
    @Singleton
    fun provideInvoiceRepository(
        @Named("RealApi") realApi: InvoiceApiService,
        @Named("MockApi") mockApi: InvoiceApiService
    ): InvoiceRepository {
        return InvoiceRepositoryImpl(realApi, mockApi)
    }

    // 5. USE CASE
    @Provides
    @Singleton
    fun provideGetInvoicesUseCase(repository: InvoiceRepository): GetInvoicesUseCase {
        return GetInvoicesUseCase(repository)
    }
}
