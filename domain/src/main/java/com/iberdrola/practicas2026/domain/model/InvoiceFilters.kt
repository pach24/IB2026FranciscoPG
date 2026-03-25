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
    val filteredStatuses: Set<InvoiceStatus> = emptySet()
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
    val activeCount: Int
        get() {
            var count = 0

            // 1. Categoría Fechas
            if (startDate != null || endDate != null) {
                count++
            }

            // 2. Categoría Estados
            if (filteredStatuses.isNotEmpty()) {
                count++
            }

            // 3. Categoría Importe (Slider)
            // Suma 1 solo si el mínimo es mayor a 0 o si el máximo ha sido modificado (no es nulo)
            if ((minAmount != null && minAmount > 0.0) || maxAmount != null) {
                count++
            }

            return count
        }

    val hasAnyFilterApplied: Boolean
        get() = minAmount != null ||
                maxAmount != null ||
                startDate != null ||
                endDate != null ||
                filteredStatuses.isNotEmpty()

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
