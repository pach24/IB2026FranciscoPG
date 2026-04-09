package com.iberdrola.practicas2026.FranciscoPG.domain.model

enum class SupplyType(val apiValue: String) {
    ELECTRICITY("LUZ"),
    GAS("GAS");

    companion object {
        fun fromApiValue(value: String): SupplyType =
            entries.firstOrNull { it.apiValue.equals(value, ignoreCase = true) }
                ?: ELECTRICITY
    }
}
