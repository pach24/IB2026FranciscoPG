package com.iberdrola.practicas2026.data.model

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice

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
        status = descEstado,
        amount = importeOrdenacion,
        chargeDate = fechaCobro,
        periodStart = fechaInicio,
        periodEnd = fechaFin
    )
