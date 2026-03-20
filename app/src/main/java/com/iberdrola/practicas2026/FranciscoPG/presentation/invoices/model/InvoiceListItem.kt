package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model

import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus

sealed class InvoiceListItem {
    data class HeaderYear(val year: String) : InvoiceListItem()

    data class InvoiceItem(
        val id: String,
        val date: String,
        val type: String,
        val amount: String,
        val statusText: String,
        val status: InvoiceStatus
    ) : InvoiceListItem()
}