package com.iberdrola.practicas2026.FranciscoPG.data.model

data class ApiResponse<T>(
    val code: Int,
    val error: String?,
    val data: T?
)
