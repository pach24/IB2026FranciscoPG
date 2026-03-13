package com.iberdrola.practicas2026.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey val id: String,
    val status: String,
    val amount: Double,
    val chargeDate: String,
    val periodStart: String,
    val periodEnd: String,
    val supplyType: String
)

fun InvoiceEntity.toDomain(): Invoice =
    Invoice(
        id = id,
        status = status,
        amount = amount,
        chargeDate = chargeDate,
        periodStart = periodStart,
        periodEnd = periodEnd
    )

fun Invoice.toEntity(supplyType: String): InvoiceEntity =
    InvoiceEntity(
        id = id,
        status = status,
        amount = amount,
        chargeDate = chargeDate,
        periodStart = periodStart,
        periodEnd = periodEnd,
        supplyType = supplyType
    )
