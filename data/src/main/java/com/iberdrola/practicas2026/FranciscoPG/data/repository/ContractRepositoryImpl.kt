package com.iberdrola.practicas2026.FranciscoPG.data.repository

import com.iberdrola.practicas2026.FranciscoPG.data.local.ContractDao
import com.iberdrola.practicas2026.FranciscoPG.data.local.toDomain
import com.iberdrola.practicas2026.FranciscoPG.data.local.toEntity
import com.iberdrola.practicas2026.FranciscoPG.data.model.toDomain
import com.iberdrola.practicas2026.FranciscoPG.data.network.ContractApiService
import com.iberdrola.practicas2026.FranciscoPG.data.network.safeAwait
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Contract
import com.iberdrola.practicas2026.FranciscoPG.domain.model.ContractStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ConfigurationRepository
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ContractRepository
import java.io.IOException
import javax.inject.Inject

class ContractRepositoryImpl @Inject constructor(
    private val mockApiService: ContractApiService,
    private val configRepository: ConfigurationRepository,
    private val contractDao: ContractDao
) : ContractRepository {

    override suspend fun getContracts(): Result<List<Contract>> {
        val cached = contractDao.getAll()
        if (cached.isNotEmpty()) {
            return Result.success(cached.map { it.toDomain() })
        }

        return if (configRepository.isMockEnabled()) {
            try {
                val response = mockApiService.getContractsCall().safeAwait()
                if (response.code == 200 && response.data != null) {
                    val contracts = response.data.map { it.toDomain() }
                    contractDao.insertAll(contracts.map { it.toEntity() })
                    Result.success(contracts)
                } else {
                    Result.failure(IOException("Error de API: ${response.error}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        } else {
            val seed = listOf(
                Contract(
                    id = "LUZ_01",
                    supplyType = SupplyType.ELECTRICITY,
                    status = ContractStatus.ACTIVE,
                    email = "usuario@email.com"
                ),
                Contract(
                    id = "GAS_01",
                    supplyType = SupplyType.GAS,
                    status = ContractStatus.INACTIVE,
                    email = null
                )
            )
            contractDao.insertAll(seed.map { it.toEntity() })
            Result.success(seed)
        }
    }

    override suspend fun updateEmail(supplyType: SupplyType, email: String) {
        contractDao.updateEmail(supplyType.apiValue, email)
    }
}
