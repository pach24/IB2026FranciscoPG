package com.iberdrola.practicas2026.FranciscoPG.domain.usecase

import javax.inject.Inject

class ValidateVerificationCodeUseCase @Inject constructor() {

    companion object {
        private const val CODE_LENGTH = 6
    }

    operator fun invoke(code: String): Boolean =
        code.length == CODE_LENGTH && code.all { it.isDigit() }
}
