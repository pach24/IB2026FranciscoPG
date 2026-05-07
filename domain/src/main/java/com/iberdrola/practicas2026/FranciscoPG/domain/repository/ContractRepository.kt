package com.iberdrola.practicas2026.FranciscoPG.domain.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Contract
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType

interface ContractRepository {
    suspend fun getContracts(): Result<List<Contract>>
    suspend fun updateEmail(supplyType: SupplyType, email: String)
}
