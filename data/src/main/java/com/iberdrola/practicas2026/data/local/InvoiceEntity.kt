package com.iberdrola.practicas2026.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType

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
        status = InvoiceStatus.fromApiValue(status),
        amount = amount,
        chargeDate = chargeDate,
        periodStart = periodStart,
        periodEnd = periodEnd,
        supplyType = SupplyType.fromApiValue(supplyType)
    )

fun Invoice.toEntity(): InvoiceEntity =
    InvoiceEntity(
        id = id,
        status = status.apiValue,
        amount = amount,
        chargeDate = chargeDate,
        periodStart = periodStart,
        periodEnd = periodEnd,
        supplyType = supplyType.apiValue
    )
