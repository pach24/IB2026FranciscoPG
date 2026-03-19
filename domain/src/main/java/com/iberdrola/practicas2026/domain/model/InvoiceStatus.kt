package com.iberdrola.practicas2026.FranciscoPG.domain.model

enum class InvoiceStatus(val apiValue: String) {
    PAID("Pagada"),
    PENDING("Pendiente de pago");

    val isPaid: Boolean get() = this == PAID

    companion object {
        fun fromApiValue(value: String): InvoiceStatus =
            entries.firstOrNull { it.apiValue.equals(value, ignoreCase = true) }
                ?: PENDING
    }
}
