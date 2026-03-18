package com.iberdrola.practicas2026.data.model

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType

data class InvoiceListResponseDto(
    val numFacturas: Int,
    val facturas: List<InvoiceDto>
)

data class InvoiceDto(
    val id: String,
    val descEstado: String,
    val importeOrdenacion: Double,
    val fechaCobro: String,
    val fechaInicio: String,
    val fechaFin: String,
    val tipoSuministro: String
)

// Mapper de DTO (data) a modelo de dominio
fun InvoiceDto.toDomain(): Invoice =
    Invoice(
        id = id,
        status = InvoiceStatus.fromApiValue(descEstado),
        amount = importeOrdenacion,
        chargeDate = fechaCobro,
        periodStart = fechaInicio,
        periodEnd = fechaFin,
        supplyType = SupplyType.fromApiValue(tipoSuministro)
    )
