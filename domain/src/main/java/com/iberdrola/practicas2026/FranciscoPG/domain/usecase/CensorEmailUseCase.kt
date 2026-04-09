package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import javax.inject.Inject

/**
 * Censura la parte local del email dejando visible la primera y última letra.
 * Si la parte local tiene 2 caracteres o menos, se censuran todos.
 *
 * Ejemplos:
 *   francisco@gmail.com  →  f*******o@gmail.com
 *   fp@a.com             →  **@a.com
 *   a@b.com              →  *@b.com
 */
class CensorEmailUseCase @Inject constructor() {

    operator fun invoke(email: String): String {
        val atIndex = email.indexOf('@')
        if (atIndex < 0) return "*".repeat(email.length)

        val local = email.substring(0, atIndex)
        val domain = email.substring(atIndex) // incluye el '@'

        val censoredLocal = when {
            local.length > 2 -> local.first() + "*".repeat(local.length - 2) + local.last()
            else -> "*".repeat(local.length)
        }

        return censoredLocal + domain
    }
}
