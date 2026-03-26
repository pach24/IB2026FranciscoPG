package com.iberdrola.practicas2026.FranciscoPG.domain.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Contract

interface ContractRepository {
    suspend fun getContracts(): Result<List<Contract>>
}
