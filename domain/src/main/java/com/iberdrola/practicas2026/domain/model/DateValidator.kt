package com.iberdrola.practicas2026.FranciscoPG.domain.model

import java.time.LocalDate

/**
 * Validador de rangos de fechas.
 *
 * Proporciona validaciones null-safe para coherencia de fechas en filtros
 * y selección de rangos temporales.
 */
object DateValidator {

    /**
     * Valida que la fecha de inicio no sea posterior a la fecha de fin.
     *
     * @param start Fecha de inicio (puede ser null)
     * @param end Fecha de fin (puede ser null)
     * @return `true` si el rango es coherente o si alguna fecha es null
     */
    fun isValidRange(start: LocalDate?, end: LocalDate?): Boolean =
        start == null || end == null || !start.isAfter(end)

    /**
     * Valida que una fecha esté dentro de los límites especificados (inclusive).
     *
     * @param date Fecha a validar (puede ser null)
     * @param min Límite inferior (puede ser null, equivale a sin límite)
     * @param max Límite superior (puede ser null, equivale a sin límite)
     * @return `true` si la fecha está dentro del rango o si [date] es null
     */
    fun isWithinBounds(date: LocalDate?, min: LocalDate?, max: LocalDate?): Boolean =
        date == null || (date.isAfterOrEqual(min) && date.isBeforeOrEqual(max))

    private fun LocalDate.isAfterOrEqual(other: LocalDate?): Boolean =
        other == null || !this.isBefore(other)

    private fun LocalDate.isBeforeOrEqual(other: LocalDate?): Boolean =
        other == null || !this.isAfter(other)
}
