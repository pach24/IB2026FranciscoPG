package com.iberdrola.practicas2026.FranciscoPG.di

import android.util.Log
import co.infinum.retromock.Retromock
import com.google.gson.GsonBuilder
import com.iberdrola.practicas2026.FranciscoPG.DeviceUtils.isEmulator
import com.iberdrola.practicas2026.data.network.InvoiceApiService
import android.content.Context
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
object NetworkModule {

    @Provides
    @Singleton
    fun provideBaseUrl(): String {
        val emulator = isEmulator()
        val url = if (emulator) {
            "http://10.0.2.2:3001/"
        } else {
            "http://localhost:3001/"
        }
        Log.d("NetworkModule", "isEmulator=$emulator, BASE_URL=$url")
        return url
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor { message ->
            Log.d("OkHttp", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request = chain.request()
                Log.d("OkHttp", "${request.method} ${request.url}")
                try {
                    val response = chain.proceed(request)
                    Log.d("OkHttp", "Response ${response.code} ${request.url}")
                    response
                } catch (e: Exception) {
                    Log.e("OkHttp", "Error connecting to ${request.url}: ${e.javaClass.simpleName}: ${e.message}")
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
}
