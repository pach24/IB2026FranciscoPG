package com.iberdrola.practicas2026.FranciscoPG.domain.model

data class Contract(
    val id: String,
    val supplyType: SupplyType,
    val status: ContractStatus,
    val email: String?
)
