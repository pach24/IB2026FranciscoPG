package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model

import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus

data class LatestInvoiceUiModel(
    val amount: String,
    val dateRange: String,
    val supplyTypeLabel: String,
    val statusText: String,
    val status: InvoiceStatus,
    val iconRes: Int
)
