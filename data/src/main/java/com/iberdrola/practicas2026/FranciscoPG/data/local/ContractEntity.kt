package com.iberdrola.practicas2026.FranciscoPG.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Contract
import com.iberdrola.practicas2026.FranciscoPG.domain.model.ContractStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType

@Entity(tableName = "contracts")
data class ContractEntity(
    @PrimaryKey val supplyType: String,
    val id: String,
    val status: String,
    val email: String?
)

fun ContractEntity.toDomain(): Contract =
    Contract(
        id = id,
        supplyType = SupplyType.fromApiValue(supplyType),
        status = ContractStatus.valueOf(status),
        email = email
    )

fun Contract.toEntity(): ContractEntity =
    ContractEntity(
        supplyType = supplyType.apiValue,
        id = id,
        status = status.name,
        email = email
    )
