package com.iberdrola.practicas2026.FranciscoPG.di

import co.infinum.retromock.Retromock
import com.google.gson.GsonBuilder
import com.iberdrola.practicas2026.FranciscoPG.DeviceUtils.isEmulator
import com.iberdrola.practicas2026.FranciscoPG.data.network.ContractApiService
import com.iberdrola.practicas2026.FranciscoPG.data.network.InvoiceApiService
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        val emulator = isEmulator()
        val url = if (emulator) {
            "https://10.0.2.2:3001/"
        } else {
            "https://localhost:3001/"
        }
        return url
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .hostnameVerifier { _,_ -> true }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideRetromock(retrofit: Retrofit, @ApplicationContext context: Context): Retromock {
        return Retromock.Builder()
            .retrofit(retrofit)
            .defaultBodyFactory { input -> context.assets.open(input) }
            .build()
    }

    @Provides
    @Singleton
    @Named("RealApi")
    fun provideRealApiService(retrofit: Retrofit): InvoiceApiService =
        retrofit.create(InvoiceApiService::class.java)

    @Provides
    @Singleton
    @Named("MockApi")
    fun provideMockApiService(retromock: Retromock): InvoiceApiService =
        retromock.create(InvoiceApiService::class.java)

    @Provides
    @Singleton
    fun provideMockContractApiService(retromock: Retromock): ContractApiService =
        retromock.create(ContractApiService::class.java)
}
