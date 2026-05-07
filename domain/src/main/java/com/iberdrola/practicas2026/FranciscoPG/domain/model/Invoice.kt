package com.iberdrola.practicas2026.FranciscoPG.domain.model

import java.time.LocalDate

data class Invoice(
    val id: String,
    val contractId: String,
    val status: InvoiceStatus,
    val amount: Double,
    val chargeDate: LocalDate,
    val periodStart: LocalDate,
    val periodEnd: LocalDate,
    val supplyType: SupplyType
)
