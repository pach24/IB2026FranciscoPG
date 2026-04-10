package com.iberdrola.practicas2026.FranciscoPG.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContractDao {

    @Query("SELECT * FROM contracts")
    suspend fun getAll(): List<ContractEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(contracts: List<ContractEntity>)

    @Query("UPDATE contracts SET email = :email WHERE supplyType = :supplyType")
    suspend fun updateEmail(supplyType: String, email: String)
}
