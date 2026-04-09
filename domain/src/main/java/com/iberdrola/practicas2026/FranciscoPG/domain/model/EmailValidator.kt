package com.iberdrola.practicas2026.FranciscoPG.domain.model

object EmailValidator {

    private val EMAIL_REGEX = Regex(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    )

    fun isValid(email: String): Boolean =
        email.isNotBlank() && EMAIL_REGEX.matches(email.trim())
}
