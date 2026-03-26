package com.iberdrola.practicas2026.FranciscoPG.data.repository

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Contract
import com.iberdrola.practicas2026.FranciscoPG.domain.model.ContractStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ContractRepository
import javax.inject.Inject

class ContractRepositoryImpl @Inject constructor() : ContractRepository {

    override suspend fun getContracts(): Result<List<Contract>> =
        Result.success(
            listOf(
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
        )
}
