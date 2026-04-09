package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model

sealed class InvoiceListUiState {
    object Loading : InvoiceListUiState()

    data class Success(
        val latestInvoice: LatestInvoiceUiModel?,
        val historyItems: List<InvoiceListItem>,
        val isFiltered: Boolean = false,
        val invoiceCount: Int
    ) : InvoiceListUiState()

    // Sin facturas en el backend para este suministro
    object Empty : InvoiceListUiState()

    // Hay facturas pero los filtros aplicados las excluyen todas
    object FilteredEmpty : InvoiceListUiState()

    data class ServerError(val message: String) : InvoiceListUiState()

    data class ConnectionError(val message: String) : InvoiceListUiState()
}
