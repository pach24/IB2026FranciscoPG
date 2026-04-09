package com.iberdrola.practicas2026.FranciscoPG.presentation.myinvoices.model

import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters

data class InvoiceFilterUIState(
    val filters: InvoiceFilters = InvoiceFilters(),
    val statistics: FilterStatistics = FilterStatistics()
) {
    data class FilterStatistics(
        val minAmount: Double = 0.0,
        val maxAmount: Double = 0.0,
        val oldestDateMillis: Long = 0L,
        val newestDateMillis: Long = 0L
    )
}
