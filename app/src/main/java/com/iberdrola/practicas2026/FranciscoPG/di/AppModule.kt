// Archivo: AppModule.kt (Ubicación: di)
package com.iberdrola.practicas2026.FranciscoPG.di

import android.content.Context
import android.util.Log
import co.infinum.retromock.Retromock
import com.google.gson.GsonBuilder
import com.iberdrola.practicas2026.FranciscoPG.DeviceUtils.isEmulator
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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        // ─── CONFIGURACIÓN DE RED ────────────────────────────────────────────
        // EMULADOR:        usa 10.0.2.2 (alias del host en AVD)
        // DISPOSITIVO FÍSICO: pon aquí la IP local de tu PC (ej: 192.168.1.100)
        //                  Encuéntrala con `ipconfig` (Win) o `ifconfig` (Mac/Linux)
        val DEVICE_HOST_IP = "192.168.1.100"   // ← CAMBIA ESTO por tu IP local
        // ────────────────────────────────────────────────────────────────────

        val emulator = isEmulator()
        val url = if (emulator) {
            "http://10.0.2.2:3001/"
        } else {
            "http://localhost:3001/"
        }
        Log.d("🌐 AppModule", "isEmulator=$emulator  →  BASE_URL=$url")
        return url
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            Log.d("🌐 OkHttp", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            // Interceptor extra para loguear la URL exacta antes de cada llamada
            .addInterceptor { chain ->
                val request = chain.request()
                Log.d("🌐 OkHttp", "→ ${request.method} ${request.url}")
                try {
                    val response = chain.proceed(request)
                    Log.d("🌐 OkHttp", "← ${response.code} ${request.url}")
                    response
                } catch (e: Exception) {
                    Log.e("🌐 OkHttp", "✗ ERROR conectando a ${request.url}: ${e.javaClass.simpleName}: ${e.message}")
                    throw e
                }
            }
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        Log.d("🌐 AppModule", "Creando Retrofit con baseUrl=$baseUrl")
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
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

    @Provides
    @Singleton
    @Named("RealApi")
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