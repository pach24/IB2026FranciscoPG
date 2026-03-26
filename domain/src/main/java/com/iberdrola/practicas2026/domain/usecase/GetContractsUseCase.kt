package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Contract
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ContractRepository
import javax.inject.Inject

class GetContractsUseCase @Inject constructor(
    private val repository: ContractRepository
) {
    suspend operator fun invoke(): Result<List<Contract>> =
        repository.getContracts()
}
