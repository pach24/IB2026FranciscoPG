package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceFilters
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.toLocalDateOrNull
import java.time.LocalDate
import javax.inject.Inject

/**
 * Caso de uso para filtrar facturas.
 * Contiene la lógica pura de negocio para aplicar filtros combinados (AND).
 */
class FilterInvoicesUseCase @Inject constructor() {

    /**
     * Aplica filtros combinados (AND) a la lista de facturas.
     * @param invoices Lista original inmutable
     * @param filters Criterios de filtrado encapsulados
     * @return Lista filtrada
     */
    operator fun invoke(
        invoices: List<Invoice>,
        filters: InvoiceFilters
    ): List<Invoice> {
        if (invoices.isEmpty()) return emptyList()

        return invoices.filter { invoice ->
            matchesStatus(invoice, filters.filteredStatuses) &&
                    matchesDateRange(invoice, filters.startDate, filters.endDate) &&
                    matchesAmountRange(invoice, filters.minAmount, filters.maxAmount)
        }
    }

    /**
     * Verifica si la factura cumple con el filtro de estado.
     */
    private fun matchesStatus(invoice: Invoice, allowedStatuses: Set<InvoiceStatus>): Boolean {
        if (allowedStatuses.isEmpty()) return true
        return invoice.status in allowedStatuses
    }

    /**
     * Verifica si la factura cumple con el filtro de fecha.
     */
    private fun matchesDateRange(
        invoice: Invoice,
        startDate: LocalDate?,
        endDate: LocalDate?
    ): Boolean {
        if (startDate == null && endDate == null) return true

        val invoiceDate = invoice.chargeDate.toLocalDateOrNull() ?: return false

        if (startDate != null && invoiceDate.isBefore(startDate)) return false
        if (endDate != null && invoiceDate.isAfter(endDate)) return false

        return true
    }

    /**
     * Verifica si la factura cumple con el filtro de importe.
     */
    private fun matchesAmountRange(
        invoice: Invoice,
        minAmount: Double?,
        maxAmount: Double?
    ): Boolean {
        val min = minAmount ?: 0.0
        val max = maxAmount ?: Double.MAX_VALUE
        return invoice.amount in min..max
    }
}
