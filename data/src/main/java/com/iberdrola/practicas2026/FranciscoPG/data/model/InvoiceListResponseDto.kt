package com.iberdrola.practicas2026.FranciscoPG.data.model

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType

data class InvoiceDto(
    val id: String,
    val contractId: String,
    val descEstado: String,
    val importeOrdenacion: Double,
    val fechaCobro: String,
    val fechaInicio: String,
    val fechaFin: String,
    val tipoSuministro: String
)

fun InvoiceDto.toDomain(): Invoice =
    Invoice(
        id = id,
        contractId = contractId,
        status = InvoiceStatus.fromApiValue(descEstado),
        amount = importeOrdenacion,
        chargeDate = fechaCobro,
        periodStart = fechaInicio,
        periodEnd = fechaFin,
        supplyType = SupplyType.fromApiValue(tipoSuministro)
    )
