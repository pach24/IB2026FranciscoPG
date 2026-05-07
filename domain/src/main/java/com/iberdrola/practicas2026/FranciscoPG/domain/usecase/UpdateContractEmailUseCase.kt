package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import com.iberdrola.practicas2026.FranciscoPG.domain.repository.ContractRepository
import javax.inject.Inject

class UpdateContractEmailUseCase @Inject constructor(
    private val repository: ContractRepository
) {
    suspend operator fun invoke(supplyType: SupplyType, email: String) {
        repository.updateEmail(supplyType, email)
    }
}
