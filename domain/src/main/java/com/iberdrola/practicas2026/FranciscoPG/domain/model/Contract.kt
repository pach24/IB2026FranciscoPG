package com.iberdrola.practicas2026.FranciscoPG.domain.model

data class Contract(
    val supplyType: SupplyType,
    val status: ContractStatus,
    val email: String?
)
