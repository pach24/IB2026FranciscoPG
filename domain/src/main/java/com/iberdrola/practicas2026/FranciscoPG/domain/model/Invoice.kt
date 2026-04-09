package com.iberdrola.practicas2026.FranciscoPG.domain.model

data class Invoice(
    val id: String,
    val status: InvoiceStatus,
    val amount: Double,
    val chargeDate: String,
    val periodStart: String,
    val periodEnd: String,
    val supplyType: SupplyType
)
