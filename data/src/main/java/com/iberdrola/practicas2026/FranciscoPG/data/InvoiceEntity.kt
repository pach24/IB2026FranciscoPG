package com.iberdrola.practicas2026.FranciscoPG.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iberdrola.practicas2026.FranciscoPG.domain.model.Invoice
import com.iberdrola.practicas2026.FranciscoPG.domain.model.InvoiceStatus
import com.iberdrola.practicas2026.FranciscoPG.domain.model.SupplyType
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Entity(tableName = "invoices")
data class InvoiceEntity(
    @PrimaryKey val id: String,
    val contractId: String,
    val status: String,
    val amount: Double,
    val chargeDate: Long,
    val periodStart: Long,
    val periodEnd: Long,
    val supplyType: String
)

fun InvoiceEntity.toDomain(): Invoice =
    Invoice(
        id = id,
        contractId = contractId,
        status = InvoiceStatus.fromApiValue(status),
        amount = amount,
        chargeDate = chargeDate.toLocalDate(),
        periodStart = periodStart.toLocalDate(),
        periodEnd = periodEnd.toLocalDate(),
        supplyType = SupplyType.fromApiValue(supplyType)
    )

fun Invoice.toEntity(): InvoiceEntity =
    InvoiceEntity(
        id = id,
        contractId = contractId,
        status = status.apiValue,
        amount = amount,
        chargeDate = chargeDate.toLong(),
        periodStart = periodStart.toLong(),
        periodEnd = periodEnd.toLong(),
        supplyType = supplyType.apiValue
    )

private fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

private fun LocalDate.toLong(): Long =
    atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
