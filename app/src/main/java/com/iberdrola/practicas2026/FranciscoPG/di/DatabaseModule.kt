package com.iberdrola.practicas2026.FranciscoPG.di

import android.content.Context
import androidx.room.Room
import com.iberdrola.practicas2026.FranciscoPG.data.local.AppDatabase
import com.iberdrola.practicas2026.FranciscoPG.data.local.ContractDao
import com.iberdrola.practicas2026.FranciscoPG.data.local.InvoiceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "iberdrola_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideInvoiceDao(database: AppDatabase): InvoiceDao {
        return database.invoiceDao()
    }

    @Provides
    @Singleton
    fun provideContractDao(database: AppDatabase): ContractDao {
        return database.contractDao()
    }
}
