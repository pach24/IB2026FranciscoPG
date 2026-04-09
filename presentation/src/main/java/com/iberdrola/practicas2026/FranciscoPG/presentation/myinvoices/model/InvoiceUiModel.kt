package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model

data class InvoiceUiModel(
    val latestInvoice: LatestInvoiceUiModel?,
    val historyItems: List<InvoiceListItem>
)
