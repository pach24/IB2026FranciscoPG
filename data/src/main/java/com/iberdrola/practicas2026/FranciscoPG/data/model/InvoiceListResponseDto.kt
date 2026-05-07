package com.iberdrola.practicas2026.FranciscoPG.data.model

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

data class InvoiceDto(
    val id: String,
    val contractId: String,
    val descEstado: String,
    val importeOrdenacion: Double,
    val fechaCobro: Long,
    val fechaInicio: Long,
    val fechaFin: Long,
    val tipoSuministro: String
)

fun InvoiceDto.toDomain(): Invoice =
    Invoice(
        id = id,
        contractId = contractId,
        status = InvoiceStatus.fromApiValue(descEstado),
        amount = importeOrdenacion,
        chargeDate = fechaCobro.toLocalDate(),
        periodStart = fechaInicio.toLocalDate(),
        periodEnd = fechaFin.toLocalDate(),
        supplyType = SupplyType.fromApiValue(tipoSuministro)
    )

private fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
