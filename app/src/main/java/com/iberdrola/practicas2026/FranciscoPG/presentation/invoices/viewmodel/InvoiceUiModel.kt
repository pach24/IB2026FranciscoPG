package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.viewmodel

import com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model.InvoiceListItem

data class InvoiceUiModel(
    val latestInvoice: LatestInvoiceUiModel?,
    val historyItems: List<InvoiceListItem>
)