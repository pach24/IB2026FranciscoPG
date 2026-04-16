package com.iberdrola.practicas2026.FranciscoPG.data.repository

import com.iberdrola.practicas2026.FranciscoPG.data.local.ContractDao
import com.iberdrola.practicas2026.FranciscoPG.data.local.toDomain
import com.iberdrola.practicas2026.FranciscoPG.data.local.toEntity
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Contract
import com.iberdrola.practicas2026.FranciscoPG.domain.model.ContractStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ContractRepository
import javax.inject.Inject

class ContractRepositoryImpl @Inject constructor(
    private val contractDao: ContractDao
) : ContractRepository {

    override suspend fun getContracts(): Result<List<Contract>> {
        val cached = contractDao.getAll()
        if (cached.isNotEmpty()) {
            return Result.success(cached.map { it.toDomain() })
        }

        val seed = listOf(
            Contract(
                supplyType = SupplyType.ELECTRICITY,
                status = ContractStatus.ACTIVE,
                email = "usuario@email.com"
            ),
            Contract(
                supplyType = SupplyType.GAS,
                status = ContractStatus.INACTIVE,
                email = null
            )
        )
        contractDao.insertAll(seed.map { it.toEntity() })
        return Result.success(seed)
    }

    override suspend fun updateEmail(supplyType: SupplyType, email: String) {
        contractDao.updateEmail(supplyType.apiValue, email)
    }
}
