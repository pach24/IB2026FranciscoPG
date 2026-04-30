package com.iberdrola.practicas2026.FranciscoPG.data.model

data class ApiEnvelopeDto<T>(
    val code: Int,
    val message: String,
    val data: T?
)
