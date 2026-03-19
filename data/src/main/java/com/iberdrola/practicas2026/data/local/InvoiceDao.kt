package com.iberdrola.practicas2026.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface InvoiceDao {

    @Query("SELECT * FROM invoices WHERE supplyType = :supplyType")
    suspend fun getInvoicesBySupplyType(supplyType: String): List<InvoiceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(invoices: List<InvoiceEntity>)

    @Query("SELECT COUNT(*) FROM invoices")
    suspend fun getCount(): Int



    @Query("DELETE FROM invoices")
    suspend fun deleteAll()
}