package com.iberdrola.practicas2026.FranciscoPG.domain.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

/**
 * Obtiene el importe máximo de la lista de facturas.
 * @return Importe máximo o 0.0 si la lista está vacía
 */
fun List<Invoice>.maxAmount(): Double =
    maxOfOrNull { it.amount } ?: 0.0

/**
 * Obtiene el importe mínimo de la lista de facturas.
 * @return Importe mínimo o 0.0 si la lista está vacía
 */
fun List<Invoice>.minAmount(): Double =
    minOfOrNull { it.amount } ?: 0.0

/**
 * Obtiene la fecha más antigua de las facturas.
 * @return LocalDate más antiguo o null si no hay fechas válidas
 */
fun List<Invoice>.oldestDate(): LocalDate? =
    mapNotNull { it.chargeDate.toLocalDateOrNull() }.minOrNull()

/**
 * Obtiene la fecha más reciente de las facturas.
 * @return LocalDate más reciente o null si no hay fechas válidas
 */
fun List<Invoice>.newestDate(): LocalDate? =
    mapNotNull { it.chargeDate.toLocalDateOrNull() }.maxOrNull()

/**
 * Convierte una fecha en formato "dd/MM/yyyy" a LocalDate.
 * @return LocalDate o null si el formato es inválido
 */
fun String.toLocalDateOrNull(): LocalDate? =
    try {
        LocalDate.parse(this, DATE_FORMATTER)
    } catch (_: Exception) {
        null
    }
