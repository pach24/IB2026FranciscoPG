package com.iberdrola.practicas2026.FranciscoPG.presentation.invoices.model

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice

sealed class InvoiceUiState {
    object Loading : InvoiceUiState()
    data class Success(val invoices: List<Invoice>) : InvoiceUiState()
    data class Error(val message: String) : InvoiceUiState()
}
