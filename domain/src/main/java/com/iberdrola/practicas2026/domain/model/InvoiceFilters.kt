package com.iberdrola.practicas2026.FranciscoPG.domain.model

import java.time.LocalDate

/**
 * Filtros para consultar facturas.
 * Todos los criterios se aplican con lógica AND.
 */
data class InvoiceFilters(
    val minAmount: Double? = null,
    val maxAmount: Double? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val filteredStatuses: Set<String> = emptySet()
) {
    /**
     * Normaliza los filtros aplicando reglas de negocio.
     *
     * Reglas aplicadas:
     * - Si startDate > endDate, se intercambian
     * - Si minAmount > maxAmount, se intercambian
     *
     * @return Copia corregida de los filtros
     */
    fun normalize(): InvoiceFilters {
        var normalized = this

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            normalized = normalized.copy(
                startDate = endDate,
                endDate = startDate
            )
        }

        val min = minAmount ?: 0.0
        val max = maxAmount ?: Double.MAX_VALUE

        if (min > max) {
            normalized = normalized.copy(
                minAmount = maxAmount,
                maxAmount = minAmount
            )
        }

        return normalized
    }
}
