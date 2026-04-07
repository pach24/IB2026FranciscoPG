package com.iberdrola.practicas2026.domain.usecase

import javax.inject.Inject

/**
 * Controla los intentos de reenvío de SMS durante el proceso de activación de factura electrónica.
 *
 * NOTA: Aunque el límite está definido como [MAX_DAILY_ATTEMPTS] (intentos "diarios"),
 * el reseteo real NO está vinculado al día calendario. El contador se resetea al salir
 * del proceso, ya que esta instancia está ligada al ciclo de vida del ViewModel.
 * Para una persistencia real por día habría que delegar en un almacenamiento externo
 * (DataStore, SharedPreferences, etc.) con la fecha del último reset.
 */
class ResendCodeUseCase @Inject constructor() {

    private var attemptsUsed = 0

    val attemptsLeft: Int
        get() = (MAX_DAILY_ATTEMPTS - attemptsUsed).coerceAtLeast(0)

    val canResend: Boolean
        get() = attemptsLeft > 0


    fun resend(): Boolean {
        if (!canResend) return false
        attemptsUsed++
        return true
    }


    fun reset() {
        attemptsUsed = 0
    }

    companion object {
        const val MAX_DAILY_ATTEMPTS = 3
    }
}