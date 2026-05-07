package com.iberdrola.practicas2026.FranciscoPG.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [InvoiceEntity::class, ContractEntity::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun invoiceDao(): InvoiceDao
    abstract fun contractDao(): ContractDao
}