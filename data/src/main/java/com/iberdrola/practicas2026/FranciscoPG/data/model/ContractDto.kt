package com.iberdrola.practicas2026.FranciscoPG.data.model

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Contract
import com.iberdrola.practicas2026.FranciscoPG.domain.model.ContractStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType

data class ContractDto(
    val id: String,
    val tipoSuministro: String,
    val estado: String,
    val email: String?
)

fun ContractDto.toDomain(): Contract =
    Contract(
        id = id,
        supplyType = SupplyType.fromApiValue(tipoSuministro),
        status = ContractStatus.valueOf(estado),
        email = email
    )
