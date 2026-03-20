package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model

data class InvoiceUiModel(
    val latestInvoice: LatestInvoiceUiModel?,
    val historyItems: List<InvoiceListItem>
)
