package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model

data class InvoicesUiState(
    val electricityState: InvoiceListUiState = InvoiceListUiState.Loading,
    val gasState: InvoiceListUiState = InvoiceListUiState.Loading,
    val activeFilterCount: Int = 0,
    val isFiltered: Boolean = false,
    val isGlobalEmpty: Boolean = false,
    val preferredTabIndex: Int = 0,
    val showBanner: Boolean = false
)
