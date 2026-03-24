package com.iberdrola.practicas2026.FranciscoPG.di

import android.content.Context
import com.iberdrola.practicas2026.FranciscoPG.data.repository.ConfigurationRepositoryImpl
import com.iberdrola.practicas2026.FranciscoPG.data.repository.FeedbackRepositoryImpl
import com.iberdrola.practicas2026.FranciscoPG.data.repository.InvoiceRepositoryImpl
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.FeedbackRepository
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.InvoiceRepository
import com.iberdrola.practicas2026.data.local.InvoiceDao
import com.iberdrola.practicas2026.data.network.InvoiceApiService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindConfigurationRepository(
        impl: ConfigurationRepositoryImpl
    ): ConfigurationRepository

    companion object {

        @Provides
        @Singleton
        fun provideInvoiceRepository(
            @Named("RealApi") realApi: InvoiceApiService,
            @Named("MockApi") mockApi: InvoiceApiService,
            configRepository: ConfigurationRepository,
            invoiceDao: InvoiceDao
        ): InvoiceRepository {
            return InvoiceRepositoryImpl(realApi, mockApi, configRepository, invoiceDao)
        }

        @Provides
        @Singleton
        fun provideFeedbackRepository(@ApplicationContext context: Context): FeedbackRepository {
            return FeedbackRepositoryImpl(context)
        }
    }
}
